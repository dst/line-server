package com.stefanski.lineserver.server.cmd;

import com.stefanski.lineserver.server.ClientHandler;
import com.stefanski.lineserver.server.resp.Response;

/**
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public interface Command {

    Response execute(ClientHandler handler);
}
