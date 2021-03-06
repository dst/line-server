package com.stefanski.liner.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Dariusz Stefanski
 * @since Sep 6, 2013
 */
@Slf4j
public abstract class Client implements Runnable {

    private final String name;
    private final int port;
    private boolean jobDone;
    private long reqCount;

    public Client(String name, int port) {
        this.name = name;
        this.port = port;
        jobDone = false;
        reqCount = 0;
    }

    @Override
    public void run() {
        log.info("Starting client: {}", name);

        try (Socket socket = new Socket(InetAddress.getLocalHost(), port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));) {

            doJob(in, out);
            registerDoneJob();

        } catch (Exception e) {
            Assert.fail("Exception: " + e);
        }

        log.info("Stopping client: ", name);
    }

    public boolean isJobDone() {
        return jobDone;
    }

    public long getRequestCount() {
        return reqCount;
    }

    /**
     * After any successful request to server, the registerRequest method must be called
     * 
     * @param in
     * @param out
     * @throws IOException
     * @throws InterruptedException
     */
    protected abstract void doJob(BufferedReader in, PrintWriter out) throws IOException,
            InterruptedException;

    protected void registerRequest() {
        reqCount++;
    }

    private void registerDoneJob() {
        jobDone = true;
    }

    public String getName() {
        return name;
    }
}
