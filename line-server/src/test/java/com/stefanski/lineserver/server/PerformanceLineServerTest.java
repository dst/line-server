package com.stefanski.lineserver.server;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stefanski.lineserver.server.client.Client;
import com.stefanski.lineserver.server.client.FastClient;
import com.stefanski.lineserver.util.StdLogger;

/**
 * Performance tests are run only if PerformanceTesting system variable is set.
 * 
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
public class PerformanceLineServerTest extends LineServerTest {

    private static final long TIME_PER_TEST_MS = 1000;

    // Only 1 instance of server because it is expensive to start it for big file
    private static Thread serverThread;

    @BeforeClass
    public static void startServer() throws Exception {
        final LineServer server = LineServerFactory.createServer(getTestFile());
        serverThread = startServer(server);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        if (isPerformanceTesting()) {
            terminateServer(serverThread);
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
