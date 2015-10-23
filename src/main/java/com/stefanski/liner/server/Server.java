package com.stefanski.liner.server;

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
    void run(String fileName);

    /**
     * Terminates server.
     */
    void shutdown();
}
