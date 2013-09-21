package com.stefanski.lineserver.server.cmd;

import com.stefanski.lineserver.server.ClientHandler;
import com.stefanski.lineserver.server.resp.EmptyResponse;
import com.stefanski.lineserver.server.resp.Response;

/**
 * This command does nothing.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class EmptyCommand implements Command {

    private static EmptyCommand INSTANCE = new EmptyCommand();

    public static EmptyCommand getInstance() {
        return INSTANCE;
    }

    private EmptyCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(ClientHandler handler) {
        return EmptyResponse.getInstance();
    }

}
