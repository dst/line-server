package com.stefanski.lineserver.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stefanski.lineserver.util.StdLogger;

/**
 * In fact, here we have integration tests, not unit tests.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class LineServerTest {

    private static List<String> finishedClients = new ArrayList<>();

    private Thread serverThread;

    @Before
    public void setUp() throws Exception {
        serverThread = startServer();
    }

    @After
    public void cleanUp() throws Exception {
        terminateServer();
        resetFinishedClients();
    }

    @Test
    public void shouldServerStopAfterReceivingShutdownMsg() throws Exception {
        terminateServer();
        // Otherwise it will be still running and test will not finish
    }

    @Test
    public void shouldServerSupportMultipleSimultaneousClients() throws Exception {
        final String name1 = "Slow Client 1";
        final String name2 = "Slow Client 2";

        Thread client1 = startClient(Client.createSlowClient(name1, 100));
        Thread client2 = startClient(Client.createSlowClient(name2, 100));

        Thread.sleep(150);

        Assert.assertTrue("One of clients not finished", isClientFinished(name1, name2));

        client1.join();
        client2.join();
    }

    private Thread startServer() throws InterruptedException {
        // start server
        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LineServer server = LineServerFactory
                            .createServer("src/test/resources/com/stefanski/lineserver/fox.txt");
                    server.run();
                } catch (LineServerException e) {
                    Assert.fail("Starting server failed: " + e);
                }
            }
        });

        server.start();

        // Wait for server start to avoid "connection refused" in clients
        Thread.sleep(100);

        return server;
    }

    private void terminateServer() throws Exception {
        startClient(Client.createShutdownClient("Terminator"));
        serverThread.join();
    }

    private Thread startClient(Client client) {
        Thread clientThread = new Thread(client);
        clientThread.start();
        return clientThread;
    }

    static private void registerDoneJob(String clientName) {
        finishedClients.add(clientName);
    }

    private boolean isClientFinished(String... clientNames) {
        for (String name : clientNames) {
            if (!finishedClients.contains(name)) {
                return false;
            }
        }
        return true;
    }

    private void resetFinishedClients() {
        finishedClients.clear();
    }

    // Fast approach, without inheritance
    static class Client implements Runnable {

        private final String name;
        private final String cmd;
        private final long delay;

        static Client createSlowClient(String name, long delay) {
            return new Client(name, "GET 1", delay);
        }

        static Client createShutdownClient(String name) {
            return new Client(name, LineServer.SHUTDOWN_CMD, 0);
        }

        public Client(String name, String cmd, long delay) {
            this.name = name;
            this.cmd = cmd;
            this.delay = delay;
        }

        @Override
        public void run() {
            try (Socket socket = new Socket(InetAddress.getLocalHost(), LineServer.TCP_PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));) {

                if (cmd != null) {
                    out.println(cmd);
                    String resp = in.readLine();
                    StdLogger.info("Cmd done by " + name + ", resp: " + resp);
                }

                if (delay != 0) {
                    StdLogger.info("Waiting by " + name);
                    Thread.sleep(delay);
                    StdLogger.info("Waiting done by " + name);
                }

                registerDoneJob(name);

            } catch (Exception e) {
                Assert.fail("Exception: " + e);
            }
        }
    }
}
