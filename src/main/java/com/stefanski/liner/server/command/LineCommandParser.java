package com.stefanski.liner.server.command;

import org.springframework.stereotype.Component;

/**
 * @author Dariusz Stefanski
 * @since 18 Oct 2015
 */
@Component
class LineCommandParser implements CommandParser {

    private static final String LINE_CMD = "LINE";

    @Override
    public boolean isApplicableTo(String line) {
        return line.startsWith(LINE_CMD);
    }

    @Override
    public Command parse(String line) {
        String[] tokens = line.split(" ");
        if (tokens.length != 2) {
            throw new CommandParserException("Invalid format of line command: " + line);
        }

        long lineNr = getLineNr(tokens[1]);
        return new LineCommand(lineNr);
    }

    private long getLineNr(String str) {
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            throw new CommandParserException("Cannot parse line number: " + str, e);
        }
    }
}
