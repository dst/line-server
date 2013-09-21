package com.stefanski.lineserver.server.comm;

/**
 * It listens for new clients.
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
     * Stops detection.
     * 
     * @throws CommunicationException
     */
    void stop() throws CommunicationException;

    /**
     * After accepting the next client, it return a communication channel to this client.
     * 
     * @return
     * @throws CommunicationException
     */
    Communication acceptNextClient() throws CommunicationException;
}
