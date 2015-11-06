package com.stefanski.liner.server.command.parser;

import org.springframework.stereotype.Component;

import com.stefanski.liner.server.command.Command;
import com.stefanski.liner.server.command.QuitCommand;

/**
 * @author Dariusz Stefanski
 * @since 18 Oct 2015
 */
@Component
class QuitCommandParser implements CommandParser {

    private static final String QUIT_CMD = "QUIT";

    @Override
    public boolean isApplicableTo(String line) {
        return line.startsWith(QUIT_CMD);
    }

    @Override
    public Command parse(String line) {
        return QuitCommand.getInstance();
    }
}
