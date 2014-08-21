package com.stefanski.liner.server;

import com.stefanski.liner.server.comm.CommunicationException;

/**
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public interface Server {

    /**
     * Runs server.
     * 
     * @throws CommunicationException
     *             If critical exception occurs during starting server.
     */
    void run() throws CommunicationException;

    /**
     * Terminates server.
     */
    void shutdown();
}
