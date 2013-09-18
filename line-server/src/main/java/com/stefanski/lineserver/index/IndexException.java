package com.stefanski.lineserver.index;

/**
 * An exception that is thrown whenever there are some problems with an index.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class IndexException extends Exception {

    private static final long serialVersionUID = 114529935544527514L;

    public IndexException(String message) {
        super(message);
    }

    public IndexException(String message, Throwable cause) {
        super(message, cause);
    }

}
