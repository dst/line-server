package com.stefanski.liner.server;

import lombok.extern.slf4j.Slf4j;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.server.cmd.Command;
import com.stefanski.liner.server.cmd.CommandContext;
import com.stefanski.liner.server.communication.Communication;
import com.stefanski.liner.server.communication.CommunicationException;
import com.stefanski.liner.server.resp.Response;

/**
 * Handles interaction with one client.
 * 
 * @author Dariusz Stefanski
 * @date Sep 11, 2013
 */
@Slf4j
public class ClientHandler implements Runnable {

    private final Server server;
    private final Communication communication;
    private final TextFile textFile;
    private boolean running;

    public ClientHandler(Server server, Communication communication, TextFile textFile) {
        this.server = server;
        this.communication = communication;
        this.textFile = textFile;
        running = true;
    }

    @Override
    public void run() {
        log.info("Handling new client");

        while (isRunning()) {
            try {
                Command cmd = communication.receiveCommand();
                CommandContext ctx = new CommandContext(this, textFile);
                Response resp = cmd.execute(ctx);
                communication.sendResponse(resp);
            } catch (CommunicationException e) {
                log.error("Problem with communication channel: " + e);
                quit();
            }
        }

        closeCommunicationChannel();
    }

    public void quit() {
        running = false;
    }

    public void shutdownServer() {
        server.shutdown();
        quit();
    }

    private boolean isRunning() {
        return running;
    }

    private void closeCommunicationChannel() {
        try {
            communication.close();
        } catch (Exception e) {
            log.error("Failed closing communication channel: " + e);
            // Ignore, we are just finishing
        }
    }
}
