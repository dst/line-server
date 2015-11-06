package com.stefanski.liner.server.command.parser;

import com.stefanski.liner.server.command.Command;

/**
 * @author Dariusz Stefanski
 * @since 18 Oct 2015
 */
interface CommandParser {

    boolean isApplicableTo(String line);

    Command parse(String line);
}
