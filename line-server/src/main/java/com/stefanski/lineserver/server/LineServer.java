package com.stefanski.lineserver.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.server.comm.Communication;
import com.stefanski.lineserver.server.comm.CommunicationDetector;
import com.stefanski.lineserver.server.comm.CommunicationException;
import com.stefanski.lineserver.util.StdLogger;

/**
 * A server that serves individual lines of an immutable text file to clients using the following
 * protocol:
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
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
public class LineServer implements Server {

    /**
     * Detects new clients.
     */
    private final CommunicationDetector detector;

    /**
     * An immutable text file which is served by this server.
     */
    private final TextFile textFile;

    /**
     * It gives us support for multiple simultaneous clients.
     */
    private final ExecutorService executor;

    /**
     * Indicates whether server is listening for clients. The server stops listening after receiving
     * SHUTDOWN message.
     */
    private volatile boolean listening;

    /**
     * @param simultaneousClientsLimit
     *            How many simultaneous clients can be handled
     * @param detector
     *            Detects new clients
     * @param textFile
     *            An immutable text file
     */
    public LineServer(int simultaneousClientsLimit, CommunicationDetector detector,
            TextFile textFile) {
        this.detector = detector;
        this.textFile = textFile;
        executor = Executors.newFixedThreadPool(simultaneousClientsLimit);
        listening = true;
    }

    /**
     * {@inheritDoc}
     */
    public void run() throws CommunicationException {
        StdLogger.info("Running server.");

        detector.start();

        while (isListening()) {
            try {
                Communication communication = detector.acceptNextClient();
                ClientHandler handler = new ClientHandler(this, communication, textFile);
                executor.execute(handler);
            } catch (CommunicationException e) {
                StdLogger.error("Exception while handling client: " + e);
                // Try to handle next client
                continue;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        StdLogger.info("Shoutdowning a server...");

        listening = false;
        executor.shutdownNow();
        try {
            textFile.close();
            detector.stop();
        } catch (Exception e) {
            StdLogger.error("Error during stoping server: " + e);
            // Ignore, we are just exiting
        }

        StdLogger.info("Server stopped");
    }

    private boolean isListening() {
        return listening;
    }
}
