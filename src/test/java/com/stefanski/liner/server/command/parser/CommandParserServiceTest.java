package com.stefanski.liner.server.command.parser;

import junit.framework.Assert;
import org.junit.Test;

import com.stefanski.liner.server.command.Command;
import com.stefanski.liner.server.command.QuitCommand;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertEquals;

/**
 * @author Dariusz Stefanski
 * @since Sep 21, 2013
 */
public class CommandParserServiceTest {

    @Test
    public void shouldParseCorrectCommand() {
        // given:
        CommandParserService parserService = new CommandParserService(asList(new QuitCommandParser()));

        // when:
        Command command = parserService.parse("QUIT");

        // then:
        Assert.assertEquals(QuitCommand.getInstance(), command);
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForUnknownCommand() {
        // given:
        CommandParserService parserService = new CommandParserService(emptyList());
        parserService.parse("aaaaaa");
    }
}
