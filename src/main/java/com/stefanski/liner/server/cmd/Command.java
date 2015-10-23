package com.stefanski.liner.server.cmd;

import com.stefanski.liner.server.resp.Response;

/**
 * A command.
 *
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public interface Command {

    /**
     * Executes a command and returns a response.
     *
     * @param ctx
     *            A context needed to run a command
     */
    Response execute(CommandContext ctx);
}
