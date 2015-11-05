package com.stefanski.liner.server.communication;

import com.stefanski.liner.server.cmd.Command;
import com.stefanski.liner.server.resp.Response;

/**
 * A communication channel between a server and clients. The server get requests via this channel
 * and sends responses.
 *
 * @author Dariusz Stefanski
 * @since Sep 12, 2013
 */
public interface Communication extends AutoCloseable {

    /**
     * Extracts next command from a communication channel.
     *
     * @return
     * @throws CommunicationException
     */
    Command receiveCommand();

    /**
     * Sends a response resp to a communication channel.
     *
     * @param resp
     * @throws CommunicationException
     */
    void sendResponse(Response resp);
}
