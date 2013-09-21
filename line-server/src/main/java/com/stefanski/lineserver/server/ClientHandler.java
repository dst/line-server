package com.stefanski.lineserver.server;

import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.server.cmd.Command;
import com.stefanski.lineserver.server.cmd.CommandContext;
import com.stefanski.lineserver.server.comm.Communication;
import com.stefanski.lineserver.server.comm.CommunicationException;
import com.stefanski.lineserver.server.resp.Response;
import com.stefanski.lineserver.util.StdLogger;

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
     * @param socket
     * @param protocol
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
            Command cmd = communication.receiveCommand();
            CommandContext ctx = new CommandContext(this, textFile);
            Response resp = cmd.execute(ctx);
            try {
                communication.sendResponse(resp);
            } catch (CommunicationException e) {
                StdLogger.error("Cannot send response: " + e);
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
