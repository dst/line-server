package com.stefanski.liner.server.cmd;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertEquals;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class CommandParserServiceTest {

    @Test
    public void shouldParseCorrectCommand() {
        // given:
        CommandParserService parserService = new CommandParserService(asList(new QuitCommandParser()));

        // when:
        Command command = parserService.parseCmd("QUIT");

        // then:
        assertEquals(QuitCommand.getInstance(), command);
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForUnknownCommand() {
        // given:
        CommandParserService parserService = new CommandParserService(emptyList());
        parserService.parseCmd("aaaaaa");
    }
}
