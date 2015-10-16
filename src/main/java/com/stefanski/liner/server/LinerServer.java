package com.stefanski.liner.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.server.comm.Communication;
import com.stefanski.liner.server.comm.CommunicationDetector;
import com.stefanski.liner.server.comm.CommunicationException;

/**
 * A server that serves specified lines of an immutable text file.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
@Slf4j
public class LinerServer implements Server {

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
    public LinerServer(int simultaneousClientsLimit, CommunicationDetector detector,
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
        log.info("Running server.");

        detector.start();

        while (isListening()) {
            try {
                Communication communication = detector.acceptNextClient();
                ClientHandler handler = new ClientHandler(this, communication, textFile);
                executor.execute(handler);
            } catch (CommunicationException e) {
                log.error("Exception while handling client: ", e);
                // Try to handle next client
                continue;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        log.info("Stopping a server...");

        listening = false;
        executor.shutdownNow();
        try {
            textFile.close();
            detector.stop();
        } catch (Exception e) {
            log.error("Error during stopping server: ", e);
            // Ignore, we are just exiting
        }

        log.info("Server stopped");
    }

    private boolean isListening() {
        return listening;
    }
}
