package com.stefanski.lineserver.server.cmd;

import com.stefanski.lineserver.server.resp.Response;

/**
 * A command.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public interface Command {

    Response execute(CommandContext ctx);
}
