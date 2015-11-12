package com.stefanski.liner.server;

/**
 * Base class for Basic and Performance test classes.
 * 
 * @author Dariusz Stefanski
 * @since Sep 6, 2013
 */
public class LinerTest {

    protected static Thread startServer(LinerServer server, String fileName)
            throws InterruptedException {

        // start server
        Thread thread = new Thread(() -> {
            try {
                server.run(fileName);
            } catch (Exception e) {
                throw new AssertionError("Starting server failed: " + e);
            }
        });

        thread.start();

        // Wait for server start to avoid "connection refused" in clients
        Thread.sleep(10);

        return thread;
    }
}
