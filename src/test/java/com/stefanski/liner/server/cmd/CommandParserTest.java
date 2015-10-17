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
    public void shouldParseGetCommand() {
        assertEquals(new LineCommand(123), parser.parseCmd("LINE 123"));
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForInvalidFormatOfGetCmd() {
        parser.parseCmd("LINE");
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForNotParsableLineNr() {
        parser.parseCmd("LINE a12a");
    }

    @Test
    public void shouldParseQuitCommand() {
        assertEquals(QuitCommand.getInstance(), parser.parseCmd("QUIT"));
    }

    @Test
    public void shouldParseShutdownCommand() {
        assertEquals(ShutdownCommand.getInstance(), parser.parseCmd("SHUTDOWN"));
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForUnknownCommand() {
        parser.parseCmd("aaaaaa");
    }
}
