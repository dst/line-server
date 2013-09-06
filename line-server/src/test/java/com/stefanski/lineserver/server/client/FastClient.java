package com.stefanski.lineserver.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import com.stefanski.lineserver.util.StdLogger;

/**
 * Client sending next random request immediately after receiving response from server.
 * 
 * It assumes that file in each line contain only line number.
 * 
 * 
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
public class FastClient extends Client {
    private final long time;
    private final int maxLineNr;

    public FastClient(String name, long time, int maxLineNr) {
        super(name);
        this.time = time;
        this.maxLineNr = maxLineNr;
    }

    @Override
    protected void doJob(BufferedReader in, PrintWriter out) throws IOException {
        long start = System.currentTimeMillis();
        Random generator = new Random();
        while (System.currentTimeMillis() - start < time) {
            long lineNr = generator.nextInt(maxLineNr) + 1;
            String cmd = "GET " + lineNr;

            out.println(cmd);

            String status = in.readLine();
            String resp = in.readLine();

            if (!(("OK").equals(status) && resp.equals(String.valueOf(lineNr)))) {
                throw new AssertionError("Invalid GET response, status: " + status + ", resp: "
                        + resp);
            }

            registerReqest();
        }

        StdLogger.info(String.format("%s did %d requests in %d ms", getName(), getReqestCount(),
                time));
    }
}