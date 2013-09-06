package com.stefanski.lineserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.stefanski.lineserver.util.StdLogger;
import com.stefanski.lineserver.util.TextFile;

/**
 * Network server that serves individual lines of an immutable text file over the network to clients
 * using the following protocol:
 * 
 * - GET nnnn
 * 
 * If nnnn is a valid line number for the given text file, return "OK\r\n" and then the nnnn-th line
 * of the specified text file.
 * 
 * If nnnn is not a valid line number for the given text file, return "ERR\r\n".
 * 
 * The first line of the file is line 1 (not line 0).
 * 
 * - QUIT
 * 
 * Disconnect client
 * 
 * - SHUTDOWN
 * 
 * Shutdown the server
 * 
 * The server listens for connections TCP port 10497.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
public class LineServer {

    // Server commands:
    public static final String GET_CMD = "GET";
    public static final String QUIT_CMD = "QUIT";
    public static final String SHUTDOWN_CMD = "SHUTDOWN";

    // TODO(dst), Sep 5, 2013: move TCP_PORT and SIMULTANEOUS_CLIENTS_LIMIT to config
    /**
     * The server listens for connections on this port.
     */
    public static final int TCP_PORT = 10497;

    /*
     * Determines how many simultaneous clients the server can handle.
     */
    private static final int SIMULTANEOUS_CLIENTS_LIMIT = 100;

    /**
     * Socket for accepting clients.
     */
    private ServerSocket serverSocket;

    /**
     * It gives us support for multiple simultaneous clients.
     */
    private final ExecutorService executor;

    /**
     * Protocol for serving GET requests.
     */
    private final LineServerProtocol protocol;

    /**
     * Indicates whether server is listening for clients. The server stops listening after receiving
     * SHUTDOWN message.
     */
    private volatile boolean listening;

    /**
     * @param textFile
     *            An immutable text file
     */
    public LineServer(TextFile textFile) {
        executor = Executors.newFixedThreadPool(SIMULTANEOUS_CLIENTS_LIMIT);
        protocol = new LineServerProtocol(textFile);
        listening = true;
    }

    /**
     * Runs server.
     * 
     * @throws LineServerException
     *             If critical exception occurs during starting server.
     */
    public void run() throws LineServerException {
        createServerSocket();
        listen();
    }

    private void createServerSocket() throws LineServerException {
        try {
            serverSocket = new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            throw new LineServerException("Could not listen on port: " + TCP_PORT, e);
        }
    }

    private void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            StdLogger.error("I/O exception when closing server socket: " + e);
            // Ignore, we are just exiting
        }

        StdLogger.info("Server stopped");
    }

    private void listen() {
        StdLogger.info(String.format("Start listening on port %s...", TCP_PORT));

        while (isListening()) {
            try {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket, protocol));
            } catch (IOException e) {
                StdLogger.error("I/O exception while handling client: " + e);
                // Try to handle next client
                continue;
            }
        }
    }

    private void shutdown() {
        StdLogger.info("Server shoutdown was requested");
        listening = false;
        executor.shutdownNow();
        stopServer();
    }

    private boolean isListening() {
        return listening;
    }

    /**
     * Handles interaction with one client using socket and protocol.
     */
    class ClientHandler implements Runnable {

        private final Socket socket;
        private final LineServerProtocol protocol;

        /**
         * @param socket
         * @param protocol
         */
        public ClientHandler(Socket socket, LineServerProtocol protocol) {
            this.socket = socket;
            this.protocol = protocol;
        }

        // TODO(dst), Sep 5, 2013: some logic can be moved to protocol
        @Override
        public void run() {
            StdLogger.info("Handling new client");

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()))) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith(GET_CMD)) {
                        try {
                            // long start = System.currentTimeMillis();

                            String response = protocol.processGetCmd(inputLine);
                            out.println(response);

                            // long elapsedTime = System.currentTimeMillis() - start;
                            // StdLogger.info("Get request responed in " + elapsedTime);
                        } catch (IllegalArgumentException e) {
                            StdLogger.error("Error while handling get command: " + e);
                            // Try to handle a next command
                            continue;
                        }
                    } else if (inputLine.startsWith(QUIT_CMD)) {
                        StdLogger.info("Disconnecting client");
                        break;
                    } else if (inputLine.startsWith(SHUTDOWN_CMD)) {
                        shutdown();
                        break;
                    } else {
                        StdLogger.error("Unknown command: " + inputLine);
                        // Try to handle a next command
                        continue;
                    }
                }
            } catch (IOException e) {
                StdLogger.error("I/O error during handling client: " + e);
                // Just close connection
            }

            closeConnection();
        }

        private void closeConnection() {
            try {
                socket.close();
            } catch (IOException e) {
                StdLogger.error("Failed closing client socket: " + e);
                // Ignore, we are just finishing
            }
        }
    }

}
