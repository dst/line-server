package com.stefanski.lineserver.server.comm;

import java.io.IOException;

import com.stefanski.lineserver.server.cmd.Command;
import com.stefanski.lineserver.server.resp.Response;

/**
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public interface Communication extends AutoCloseable {

    Command receiveCommand();

    void sendResponse(Response resp) throws IOException;
}
