package com.stefanski.lineserver.server;

import junit.framework.Assert;

import com.stefanski.lineserver.server.client.Client;
import com.stefanski.lineserver.server.client.SingleCmdClient;

/**
 * Base class for Basic and Performance test classes.
 * 
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
public class LineServerTest {

    protected static Thread startServer(final LineServer server) throws InterruptedException,
            LineServerException {

        // start server
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.run();
                } catch (LineServerException e) {
                    Assert.fail("Starting server failed: " + e);
                }
            }
        });

        thread.start();

        // Wait for server start to avoid "connection refused" in clients
        Thread.sleep(10);

        return thread;
    }

    protected static void terminateServer(Thread serverThread) throws Exception {
        startClient(SingleCmdClient.createShutdownClient("Terminator"));
        serverThread.join();
    }

    protected static Thread startClient(Client client) {
        Thread clientThread = new Thread(client);
        clientThread.start();
        return clientThread;
    }

}
