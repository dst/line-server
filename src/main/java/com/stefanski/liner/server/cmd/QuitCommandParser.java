package com.stefanski.liner.server.cmd;

import org.springframework.stereotype.Component;

/**
 * @author Dariusz Stefanski
 * @date 18 Oct 2015
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
