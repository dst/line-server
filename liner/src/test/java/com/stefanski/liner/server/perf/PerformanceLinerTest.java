package com.stefanski.liner.server.perf;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stefanski.liner.server.LinerServer;
import com.stefanski.liner.server.LinerServerFactory;
import com.stefanski.liner.server.LinerTest;
import com.stefanski.liner.util.StdLogger;

/**
 * Performance tests are run only if PerformanceTesting system variable is set.
 * 
 * It is not a unit test, it operates on real files and communicates using real sockets.
 * 
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
public class PerformanceLinerTest extends LinerTest {

    private static final long TIME_PER_TEST_MS = 1000;

    // Only 1 instance of a server because it is expensive to start it for a big file
    private static Thread serverThread;

    @BeforeClass
    public static void startServer() throws Exception {
        if (isPerformanceTesting()) {
            final LinerServer server = LinerServerFactory.createServer(getTestFile());
            serverThread = startServer(server);
        }
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        if (isPerformanceTesting()) {
            terminateServer();
        }
    }

    @Before
    public void setUp() throws Exception {
        // Run each test only in case of performance testing.
        Assume.assumeTrue(isPerformanceTesting());
    }

    @Test
    public void shouldServerHandleManyRequestsPerSecondWith1Client() throws Exception {
        runSimultaneousClients(1);
    }

    @Test
    public void shouldServerHandleManyRequestsPerSecondWith2ConcurrentClients() throws Exception {
        runSimultaneousClients(2);
    }

    @Test
    public void shouldServerHandleManyRequestsPerSecondWithManyConcurrentClients() throws Exception {
        runSimultaneousClients(100);
    }

    private void runSimultaneousClients(int clientCount) throws InterruptedException {
        Client[] clients = new Client[clientCount];
        Thread[] threads = new Thread[clientCount];

        for (int i = 0; i < clientCount; i++) {
            String name = "Fast Guy " + i;
            long runningTime = TIME_PER_TEST_MS;
            Client client = new FastClient(name, runningTime, getMaxLineNr());
            clients[i] = client;
            threads[i] = startClient(client);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (Client client : clients) {
            Assert.assertTrue(client.getName() + " didn't finish", client.isJobDone());
        }

        printStats("Client count: " + clientCount, Arrays.asList(clients));
    }

    private static void terminateServer() throws Exception {
        startClient(SingleCmdClient.createShutdownClient("Terminator"));
        serverThread.join();
    }

    private static Thread startClient(Client client) {
        Thread clientThread = new Thread(client);
        clientThread.start();
        return clientThread;
    }

    private static boolean isPerformanceTesting() {
        boolean flag = System.getenv("PerformanceTesting") != null;
        if (!flag) {
            StdLogger.info("Ignoring performance test");
        }
        return flag;
    }

    private static String getTestFile() {
        String file = System.getenv("TestFile");
        if (file == null) {
            Assert.fail("Missing TestFile in env");
        }
        return file;
    }

    private int getMaxLineNr() {
        String value = System.getenv("MaxLineNr");
        if (value == null) {
            Assert.fail("Missing MaxLineNr in env");
        }
        return Integer.valueOf(value);
    }

    private void printStats(String scenario, List<Client> clients) {
        long reqSum = 0;
        for (Client client : clients) {
            reqSum += client.getReqestCount();
        }

        long reqPerSec = reqSum / (TIME_PER_TEST_MS / 1000);
        StdLogger.info(String.format("%s, handled %d requests in %d ms (%d req/s)", scenario,
                reqSum, TIME_PER_TEST_MS, reqPerSec));
    }
}
