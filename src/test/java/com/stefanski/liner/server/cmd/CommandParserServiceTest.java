package com.stefanski.liner.server.cmd;

import org.junit.Test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class CommandParserServiceTest {

    //TODO(dst), 18.10.15: move these tests to parser specific tests
    private final CommandParserService parser = new CommandParserService(
            asList(new LineCommandParser(), new QuitCommandParser(), new ShutdownCommandParser())
    );

    @Test
    public void shouldParseGetCommand() {
        assertEquals(new LineCommand(123), parser.parseCmd("LINE 123"));
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForInvalidFormatOfGetCmd() {
        parser.parseCmd("LINE");
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForNotValidLineNr() {
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
