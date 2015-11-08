package com.stefanski.liner.server.command;

import com.stefanski.liner.server.response.EmptyResponse;
import com.stefanski.liner.server.response.Response;

/**
 * This command does nothing.
 *
 * @author Dariusz Stefanski
 * @since Sep 12, 2013
 */
public class EmptyCommand implements Command {

    private static final EmptyCommand INSTANCE = new EmptyCommand();

    public static EmptyCommand getInstance() {
        return INSTANCE;
    }

    private EmptyCommand() {
    }

    @Override
    public Response execute(CommandContext ctx) {
        return EmptyResponse.getInstance();
    }
}
