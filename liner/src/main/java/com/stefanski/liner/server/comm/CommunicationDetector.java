package com.stefanski.liner.server.comm;

/**
 * It listens (detects) for new clients.
 * 
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public interface CommunicationDetector {

    /**
     * Prepares for detecting clients.
     * 
     * @throws CommunicationException
     */
    void start() throws CommunicationException;

    /**
     * Stops a detection.
     * 
     * @throws CommunicationException
     */
    void stop() throws CommunicationException;

    /**
     * After accepting the next client, it returns a communication channel to this client.
     * 
     * @return
     * @throws CommunicationException
     */
    Communication acceptNextClient() throws CommunicationException;
}
