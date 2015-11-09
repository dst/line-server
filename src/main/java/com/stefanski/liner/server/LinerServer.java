package com.stefanski.liner.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.file.TextFileFactory;
import com.stefanski.liner.server.communication.Communication;
import com.stefanski.liner.server.communication.CommunicationException;
import com.stefanski.liner.server.communication.TCPCommunicationDetector;

/**
 * A server that serves specified lines of an immutable text file.
 *
 * @author Dariusz Stefanski
 * @since Sep 1, 2013
 */
@Slf4j
@Component
public class LinerServer {

    /**
     * Detects new clients.
     */
    private final TCPCommunicationDetector detector;

    private final TextFileFactory textFileFactory;

    /**
     * An immutable text file which is served by this server.
     */
    private TextFile textFile;

    /**
     * It gives us support for multiple simultaneous clients.
     */
    private ExecutorService executor;

    /**
     * Indicates whether server is listening for clients. The server stops listening after receiving
     * SHUTDOWN message.
     */
    private volatile boolean listening;

    @Autowired
    LinerServer(@Value("${server.simultaneous.clients.limit}") int simultaneousClientsLimit,
                TCPCommunicationDetector detector, TextFileFactory textFileFactory) {
        this.detector = detector;
        this.textFileFactory = textFileFactory;
        this.listening = true;
        createExecutor(simultaneousClientsLimit);
    }

    private void createExecutor(int count) {
        log.info("Creating thread pool with {} threads", count);
        this.executor = Executors.newFixedThreadPool(count);
    }

    /**
     * Runs server.
     *
     * @throws CommunicationException If critical exception occurs during starting server.
     */
    public void run(String fileName) {
        log.info("Running server.");
        detector.start();
        createTextFile(fileName);
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

    private void createTextFile(String fileName) {
        this.textFile = textFileFactory.createFromFile(fileName);
    }

    /**
     * Terminates server.
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
