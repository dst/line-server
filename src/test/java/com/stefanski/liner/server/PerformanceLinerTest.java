package com.stefanski.liner.server;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stefanski.liner.Application;
import com.stefanski.liner.server.client.Client;
import com.stefanski.liner.server.client.FastClient;
import com.stefanski.liner.server.client.SingleCmdClient;

/**
 * Performance tests are run only if PerformanceTesting system variable is set.
 *
 * It is not a unit test, it operates on real files and communicates using real sockets.
 *
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class PerformanceLinerTest extends LinerTest {

    private static final long TIME_PER_TEST_MS = 1000;

    @Autowired
    private LinerServer server;

    // Only 1 instance of a server because it is expensive to start it for a big file
    private static Thread serverThread;

    @Before
    public void startServer() throws Exception {
        if (isPerformanceTesting()) {
            if (serverThread == null) {
                serverThread = startServer(server, getTestFile());
            }
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
        List<Client> clients = new LinkedList<>();
        List<Thread> threads = new LinkedList<>();

        for (int i = 0; i < clientCount; i++) {
            String name = "Fast Guy " + i;
            long runningTime = TIME_PER_TEST_MS;
            Client client = new FastClient(name, runningTime, getMaxLineNr());
            clients.add(client);
            threads.add(startClient(client));
        }

        for (Thread thread: threads) {
            thread.join();
        }

        clients.forEach(client-> {
            Assert.assertTrue(client.getName() + " didn't finish", client.isJobDone());
        });

        printStats("Client count: " + clientCount, clients);
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
            log.info("Ignoring performance test");
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
        long reqSum = clients.stream().mapToLong(Client::getRequestCount).sum();
        long reqPerSec = reqSum / (TIME_PER_TEST_MS / 1000);
        log.info("{}, handled {} requests in {} ms ({} req/s)",
                scenario, reqSum, TIME_PER_TEST_MS, reqPerSec);
    }
}
