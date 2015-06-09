package com.stefanski.liner.server.comm;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class CommunicationException extends Exception {

    private static final long serialVersionUID = 6199039673545472405L;

    public CommunicationException() {
        super();
    }

    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}