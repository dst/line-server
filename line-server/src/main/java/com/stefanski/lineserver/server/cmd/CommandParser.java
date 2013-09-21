package com.stefanski.lineserver.server.cmd;

import com.stefanski.lineserver.util.StdLogger;

/**
 * Parses commands from String.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class CommandParser {

    // Commands:
    private static final String GET_CMD = "GET";
    private static final String QUIT_CMD = "QUIT";
    private static final String SHUTDOWN_CMD = "SHUTDOWN";

    public Command parseCmd(String line) throws CommandParserException {
        if (line.startsWith(GET_CMD)) {
            return parseGetCmd(line);
        } else if (line.startsWith(QUIT_CMD)) {
            return QuitCommand.getInstance();
        } else if (line.startsWith(SHUTDOWN_CMD)) {
            return ShutdownCommand.getInstance();
        } else {
            StdLogger.error("Unknown command: " + line);
            return EmptyCommand.getInstance();
        }
    }

    private Command parseGetCmd(String line) throws CommandParserException {
        String[] tokens = line.split(" ");
        if (tokens.length != 2) {
            throw new CommandParserException("Invalid format of get command: " + line);
        }

        long lineNr;
        try {
            lineNr = Long.valueOf(tokens[1]);
        } catch (NumberFormatException e) {
            throw new CommandParserException("Cannot parse line number: " + tokens[1], e);
        }

        return new GetCommand(lineNr);
    }

}
