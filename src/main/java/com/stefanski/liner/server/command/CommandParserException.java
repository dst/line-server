package com.stefanski.liner.server.command;

/**
 * An exception that is thrown whenever there are some problems during parsing commands.
 *
 * @author Dariusz Stefanski
 * @since Sep 12, 2013
 */
public class CommandParserException extends RuntimeException {

    private static final long serialVersionUID = -7127267099487369234L;

    public CommandParserException(String message) {
        super(message);
    }

    public CommandParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
