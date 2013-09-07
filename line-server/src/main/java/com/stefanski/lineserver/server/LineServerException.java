package com.stefanski.lineserver.server;

/**
 * This is an exception that is thrown whenever there are some problems with the line server.
 * 
 * @author Dariusz Stefanski
 * @date Sep 2, 2013
 */
public class LineServerException extends Exception {

    private static final long serialVersionUID = -6569023245031873948L;

    public LineServerException(String message) {
        super(message);
    }

    public LineServerException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "LineServerException [message=" + getMessage() + ", cause=" + getCause() + "]";
    }

}
