package com.stefanski.liner.server;

import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.communication.CommunicationException;

/**
 * Base class for Basic and Performance test classes.
 * 
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
public class LinerTest {

    protected static Thread startServer(LinerServer server, String fileName)
            throws InterruptedException {

        // start server
        Thread thread = new Thread(() -> {
            try {
                server.run(fileName);
            } catch (TextFileException | CommunicationException e) {
                throw new AssertionError("We have a problem. Starting server failed: " + e);
            }
        });

        thread.start();

        // Wait for server start to avoid "connection refused" in clients
        Thread.sleep(10);

        return thread;
    }
}
