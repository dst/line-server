package com.stefanski.lineserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
class LineServer {

    /**
     * The server listens for connections on this port.
     */
    private static final int TCP_PORT = 10497;

    // Protocol commands:
    private static final String GET_CMD = "GET";
    private static final String QUIT_CMD = "QUIT";
    private static final String SHUTDOWN_CMD = "SHUTDOWN";

    private final LineServerProtocol protocol;

    /**
     * Indicates whether server is listening for clients. It stops listening after receiving
     * SHUTDOWN message.
     */
    private boolean listening;

    /**
     * Factory for easy creating servers.
     * 
     * @param fileName
     *            The name of an immutable text file
     * @return A new line server
     * @throws LineServerException
     *             If server cannot be created
     */
    // TODO(dst), Sep 3, 2013: move to separate class
    public static LineServer create(String fileName) throws LineServerException {
        try {
            TextFile textFile = new TextFile(fileName);
            return new LineServer(textFile);
        } catch (IOException e) {
            throw new LineServerException("Cannot create TextFile", e);
        }
    }

    /**
     * @param textFile
     *            An immutable text file
     */
    public LineServer(TextFile textFile) {
        protocol = new LineServerProtocol(textFile);
        listening = true;
    }

    /**
     * Runs server.
     * 
     * @throws LineServerException
     *             If critical exception occurs durng starting server.
     */
    public void run() throws LineServerException {
        StdLogger.info(String.format("Start listening on port %s ...", TCP_PORT));

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            throw new LineServerException("Could not listen on port: " + TCP_PORT, e);
        }

        while (isListening()) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                StdLogger.error("Accept failed: " + e);
                // Try to handle next client
                continue;
            }

            try {
                handleClient(clientSocket);
            } catch (IOException e) {
                StdLogger.error("I/O exception while handling client: " + e);
                // Try to handle next client
                continue;
            }
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            StdLogger.error("I/O exception when closing server socket: " + e);
            // Ignore, we are just exiting
        }

        StdLogger.info("Server stopped.");
    }

    /**
     * @param clientSocket
     * @throws IOException
     */
    private void handleClient(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith(GET_CMD)) {
                try {
                    String response = protocol.processGetCmd(inputLine);
                    out.println(response);
                } catch (IllegalArgumentException iae) {
                    StdLogger.error("Error while handling get command: " + iae);
                    // Try to handle a next command
                    continue;
                }
            } else if (inputLine.startsWith(QUIT_CMD)) {
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
        out.close();
        in.close();
        clientSocket.close();
    }

    private void shutdown() {
        listening = false;
    }

    private boolean isListening() {
        return listening;
    }
}
