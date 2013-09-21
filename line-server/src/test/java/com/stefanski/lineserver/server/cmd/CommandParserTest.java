package com.stefanski.lineserver.server.cmd;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class CommandParserTest {

    private final CommandParser parser = new CommandParser();

    @Test
    public void shouldParseGetCommand() throws CommandParserException {
        assertEquals(new GetCommand(123), parser.parseCmd("GET 123"));
    }

    @Test
    public void shouldParseQuitCommand() throws CommandParserException {
        assertEquals(QuitCommand.getInstance(), parser.parseCmd("QUIT"));
    }

    @Test
    public void shouldParseShutdownCommand() throws CommandParserException {
        assertEquals(ShutdownCommand.getInstance(), parser.parseCmd("SHUTDOWN"));
    }

    @Test
    public void shouldReturnEmptyCommandForUnknownCommand() throws CommandParserException {
        assertEquals(EmptyCommand.getInstance(), parser.parseCmd("aaaaaa"));
    }
}
