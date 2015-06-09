package com.stefanski.liner.server.cmd;

import com.stefanski.liner.server.resp.EmptyResponse;
import com.stefanski.liner.server.resp.Response;

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
    public Response execute(CommandContext ctx) {
        return EmptyResponse.getInstance();
    }
}
