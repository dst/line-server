package com.stefanski.liner.server.communication;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class CommunicationException extends RuntimeException {

    private static final long serialVersionUID = 6199039673545472405L;

    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
