package com.stefanski.lineserver.server.cmd;

import com.stefanski.lineserver.server.resp.Response;

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
     * @return
     */
    Response execute(CommandContext ctx);
}
