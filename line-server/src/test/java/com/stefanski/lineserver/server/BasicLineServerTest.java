package com.stefanski.lineserver.server;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stefanski.lineserver.server.client.Client;
import com.stefanski.lineserver.server.client.SingleCmdClient;

/**
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class BasicLineServerTest extends LineServerTest {

    private Thread serverThread;

    @Before
    public void setUp() throws Exception {
        serverThread = startServer("src/test/resources/com/stefanski/lineserver/fox.txt");
    }

    @After
    public void cleanUp() throws Exception {
        terminateServer(serverThread);
    }

    @Test
    public void shouldServerStopAfterReceivingShutdownMsg() throws Exception {
        terminateServer(serverThread);
        // Otherwise it will be still running and test will not finish
    }

    @Test
    public void shouldServerSupportMultipleSimultaneousClients() throws Exception {
        Client client1 = SingleCmdClient.createSlowClient("Slow Client 1", 100);
        Client client2 = SingleCmdClient.createSlowClient("Slow Client 2", 100);

        Thread thread1 = startClient(client1);
        Thread thread2 = startClient(client2);

        Thread.sleep(150);

        Assert.assertTrue("One of clients not finished", client1.isJobDone() && client2.isJobDone());

        thread1.join();
        thread2.join();
    }
}
