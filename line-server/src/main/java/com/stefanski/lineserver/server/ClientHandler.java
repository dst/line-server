package com.stefanski.lineserver.server;

import java.io.IOException;

import com.stefanski.lineserver.server.cmd.Command;
import com.stefanski.lineserver.server.comm.Communication;
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
    private final LineServerProtocol protocol;
    private boolean running;

    /**
     * @param socket
     * @param protocol
     */
    public ClientHandler(Server server, Communication communication, LineServerProtocol protocol) {
        this.server = server;
        this.communication = communication;
        this.protocol = protocol;
        running = true;
    }

    @Override
    public void run() {
        StdLogger.info("Handling new client");

        while (isRunning()) {
            Command cmd = communication.receiveCommand();
            Response resp = cmd.execute(this);
            try {
                communication.sendResponse(resp);
            } catch (IOException e) {
                StdLogger.error("I/O error during sending response: " + e);
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

    // TODO(dst), Sep 12, 2013: this shouldn't be needed
    public LineServerProtocol getProtocol() {
        return protocol;
    }
}
