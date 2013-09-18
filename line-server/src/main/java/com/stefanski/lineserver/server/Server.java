package com.stefanski.lineserver.server;

/**
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public interface Server {

    // TODO(dst), Sep 16, 2013: LineServerException in Server: bad
    /**
     * Runs server.
     * 
     * @throws LineServerException
     *             If critical exception occurs during starting server.
     */
    void run() throws LineServerException;

    /**
     * Terminates server.
     */
    void shutdown();
}
