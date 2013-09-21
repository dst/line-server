package com.stefanski.lineserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.server.comm.Communication;
import com.stefanski.lineserver.server.comm.SocketCommunication;
import com.stefanski.lineserver.util.StdLogger;

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
public class LineServer implements Server {

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
     * An immutable text file which is served by this server.
     */
    private final TextFile textFile;

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
        this.textFile = textFile;
        executor = Executors.newFixedThreadPool(SIMULTANEOUS_CLIENTS_LIMIT);
        listening = true;
    }

    /**
     * {@inheritDoc}
     */
    public void run() throws LineServerException {
        createServerSocket();
        listen();
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        StdLogger.info("Server shoutdown was requested");
        listening = false;
        executor.shutdownNow();
        stopServer();
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
                Communication communication = SocketCommunication.fromSocket(clientSocket);
                ClientHandler handler = new ClientHandler(this, communication, textFile);
                executor.execute(handler);
            } catch (IOException e) {
                StdLogger.error("I/O exception while handling client: " + e);
                // Try to handle next client
                continue;
            }
        }
    }

    private boolean isListening() {
        return listening;
    }
}
