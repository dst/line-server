package com.stefanski.lineserver.server;

/**
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
}
