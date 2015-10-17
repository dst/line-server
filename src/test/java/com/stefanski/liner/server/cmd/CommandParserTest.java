package com.stefanski.liner.server.cmd;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class CommandParserTest {

    private final CommandParser parser = new CommandParser();

    @Test
    public void shouldParseGetCommand() throws CommandParserException {
        assertEquals(new LineCommand(123), parser.parseCmd("LINE 123"));
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForInvalidFormatOfGetCmd() throws CommandParserException {
        parser.parseCmd("LINE");
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForNotParsableLineNr() throws CommandParserException {
        parser.parseCmd("LINE a12a");
    }

    @Test
    public void shouldParseQuitCommand() throws CommandParserException {
        assertEquals(QuitCommand.getInstance(), parser.parseCmd("QUIT"));
    }

    @Test
    public void shouldParseShutdownCommand() throws CommandParserException {
        assertEquals(ShutdownCommand.getInstance(), parser.parseCmd("SHUTDOWN"));
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForUnknownCommand() throws CommandParserException {
        parser.parseCmd("aaaaaa");
    }
}
