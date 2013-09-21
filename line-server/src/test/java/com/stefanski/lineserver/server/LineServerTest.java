package com.stefanski.lineserver.server;

import com.stefanski.lineserver.server.comm.CommunicationException;

/**
 * Base class for Basic and Performance test classes.
 * 
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
public class LineServerTest {

    protected static Thread startServer(final LineServer server) throws InterruptedException {

        // start server
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.run();
                } catch (CommunicationException e) {
                    throw new AssertionError("We have a problem. Starting server failed: " + e);
                }
            }
        });

        thread.start();

        // Wait for server start to avoid "connection refused" in clients
        Thread.sleep(10);

        return thread;
    }
}
