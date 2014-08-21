package com.stefanski.liner.server;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.server.cmd.Command;
import com.stefanski.liner.server.cmd.CommandContext;
import com.stefanski.liner.server.comm.Communication;
import com.stefanski.liner.server.comm.CommunicationException;
import com.stefanski.liner.server.resp.Response;
import com.stefanski.liner.util.StdLogger;

/**
 * Handles interaction with one client using socket and protocol.
 * 
 * @author Dariusz Stefanski
 * @date Sep 11, 2013
 */
public class ClientHandler implements Runnable {

    private final Server server;
    private final Communication communication;
    private final TextFile textFile;
    private boolean running;

    /**
     * 
     * @param server
     * @param communication
     * @param textFile
     */
    public ClientHandler(Server server, Communication communication, TextFile textFile) {
        this.server = server;
        this.communication = communication;
        this.textFile = textFile;
        running = true;
    }

    @Override
    public void run() {
        StdLogger.info("Handling new client");

        while (isRunning()) {
            try {
                Command cmd = communication.receiveCommand();
                CommandContext ctx = new CommandContext(this, textFile);
                Response resp = cmd.execute(ctx);
                communication.sendResponse(resp);
            } catch (CommunicationException e) {
                StdLogger.error("Problem with communication channel: " + e);
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
            StdLogger.error("Failed closing communication channel: " + e);
            // Ignore, we are just finishing
        }
    }
}
