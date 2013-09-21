package com.stefanski.lineserver.server.perf;

import static com.stefanski.lineserver.server.comm.TCPCommunicationDetector.TCP_PORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import junit.framework.Assert;

/**
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
abstract class Client implements Runnable {

    private final String name;
    private boolean jobDone;
    private long reqCount;

    public Client(String name) {
        this.name = name;
        jobDone = false;
        reqCount = 0;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), TCP_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));) {

            doJob(in, out);
            registerDoneJob();

        } catch (Exception e) {
            Assert.fail("Exception: " + e);
        }
    }

    public boolean isJobDone() {
        return jobDone;
    }

    public long getReqestCount() {
        return reqCount;
    }

    /**
     * After any successful request to server, the registerReqest method must be called
     * 
     * @param in
     * @param out
     * @throws IOException
     * @throws InterruptedException
     */
    protected abstract void doJob(BufferedReader in, PrintWriter out) throws IOException,
            InterruptedException;

    protected void registerReqest() {
        reqCount++;
    }

    private void registerDoneJob() {
        jobDone = true;
    }

    public String getName() {
        return name;
    }
}
