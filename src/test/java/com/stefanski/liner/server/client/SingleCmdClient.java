package com.stefanski.liner.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Dariusz Stefanski
 * @date Sep 6, 2013
 */
@Slf4j
public class SingleCmdClient extends Client {

    private final String cmd;
    private final long delay;

    public static Client createSlowClient(String name, int port, long delay) {
        return new SingleCmdClient(name, port, "LINE 1", delay);
    }

    public static Client createShutdownClient(String name, int port) {
        return new SingleCmdClient(name, port, "SHUTDOWN", 0);
    }

    private SingleCmdClient(String name, int port, String cmd, long delay) {
        super(name, port);
        this.cmd = cmd;
        this.delay = delay;
    }

    @Override
    protected void doJob(BufferedReader in, PrintWriter out) throws IOException,
            InterruptedException {
        out.println(cmd);
        String resp = in.readLine();
        log.info("Cmd done by {}, resp: {}", getName(), resp);

        if (delay != 0) {
            log.info("Waiting by {}", getName());
            Thread.sleep(delay);
            log.info("Waiting done by {}", getName());
        }
    }

}
