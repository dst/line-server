package com.stefanski.lineserver.server.cmd;

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

    /**
     * Parses a given line to a command
     * 
     * @param line
     * @return
     * @throws CommandParserException
     */
    public Command parseCmd(String line) throws CommandParserException {
        assert line != null;

        if (line.startsWith(GET_CMD)) {
            return parseGetCmd(line);
        } else if (line.startsWith(QUIT_CMD)) {
            return QuitCommand.getInstance();
        } else if (line.startsWith(SHUTDOWN_CMD)) {
            return ShutdownCommand.getInstance();
        } else {
            throw new CommandParserException("Unknown command: " + line);
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
