package com.stefanski.lineserver.server.cmd;

import com.stefanski.lineserver.server.ClientHandler;
import com.stefanski.lineserver.server.resp.EmptyResponse;
import com.stefanski.lineserver.server.resp.Response;
import com.stefanski.lineserver.util.StdLogger;

/**
 * A request to disconnect client.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class QuitCommand implements Command {

    private static QuitCommand INSTANCE = new QuitCommand();

    public static QuitCommand getInstance() {
        return INSTANCE;
    }

    private QuitCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(ClientHandler handler) {
        StdLogger.info("Disconnecting client");
        handler.quit();
        return EmptyResponse.getInstance();
    }
}
