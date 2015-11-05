package com.stefanski.liner.file;

/**
 * This is an exception that is thrown whenever there are some problems with getting line from
 * TextFile.
 *
 * @author Dariusz Stefanski
 * @since Sep 7, 2013
 */
public class TextFileException extends RuntimeException {

    private static final long serialVersionUID = 2687756341553547329L;

    public TextFileException(String message) {
        super(message);
    }

    public TextFileException(String message, Throwable cause) {
        super(message, cause);
    }

}
