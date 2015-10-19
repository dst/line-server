package com.stefanski.liner.server.communication;

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
    void start();

    /**
     * Stops a detection.
     * 
     * @throws CommunicationException
     */
    void stop();

    /**
     * After accepting the next client, it returns a communication channel to this client.
     * 
     * @return
     * @throws CommunicationException
     */
    Communication acceptNextClient();
}
