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

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(ClientHandler handler) {
        return new EmptyResponse();
    }

}
