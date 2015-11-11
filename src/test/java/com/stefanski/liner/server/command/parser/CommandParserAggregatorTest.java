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
public class CommandParserAggregatorTest {

    @Test
    public void shouldParseCorrectCommand() {
        // given:
        CommandParserAggregator parserService = new CommandParserAggregator(asList(new QuitCommandParser()));

        // when:
        Command command = parserService.parse("QUIT");

        // then:
        Assert.assertEquals(QuitCommand.getInstance(), command);
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForUnknownCommand() {
        // given:
        CommandParserAggregator parserService = new CommandParserAggregator(emptyList());
        parserService.parse("aaaaaa");
    }
}
