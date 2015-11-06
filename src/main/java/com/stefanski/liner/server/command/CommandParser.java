package com.stefanski.liner.server.command;

/**
 * @author Dariusz Stefanski
 * @since 18 Oct 2015
 */
interface CommandParser {

    boolean isApplicableTo(String line);

    Command parse(String line);
}
