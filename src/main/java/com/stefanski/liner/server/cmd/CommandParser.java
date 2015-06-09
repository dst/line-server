package com.stefanski.liner.server.cmd;

/**
 * Parses commands from String.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class CommandParser {

    // Commands:
    private static final String LINE_CMD = "LINE";
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

        if (line.startsWith(LINE_CMD)) {
            return parseLineCmd(line);
        } else if (line.startsWith(QUIT_CMD)) {
            return QuitCommand.getInstance();
        } else if (line.startsWith(SHUTDOWN_CMD)) {
            return ShutdownCommand.getInstance();
        } else {
            throw new CommandParserException("Unknown command: " + line);
        }
    }

    private Command parseLineCmd(String line) throws CommandParserException {
        String[] tokens = line.split(" ");
        if (tokens.length != 2) {
            throw new CommandParserException("Invalid format of get command: " + line);
        }

        long lineNr = getLineNr(tokens[1]);
        return new LineCommand(lineNr);
    }

    private long getLineNr(String str) throws CommandParserException {
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            throw new CommandParserException("Cannot parse line number: " + str, e);
        }
    }
}
