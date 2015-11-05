package com.stefanski.liner.index;

/**
 * An exception that is thrown whenever there are some problems with an index.
 *
 * @author Dariusz Stefanski
 * @since Sep 4, 2013
 */
public class IndexException extends RuntimeException {

    private static final long serialVersionUID = 114529935544527514L;

    public IndexException(String message, Throwable cause) {
        super(message, cause);
    }
}
