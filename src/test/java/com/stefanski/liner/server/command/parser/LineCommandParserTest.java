package com.stefanski.liner.server.command.parser;

import junit.framework.Assert;
import org.junit.Test;

import com.stefanski.liner.server.command.LineCommand;
import com.stefanski.liner.server.command.parser.CommandParserException;
import com.stefanski.liner.server.command.parser.LineCommandParser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Dariusz Stefanski
 * @since 18 Oct 2015
 */
public class LineCommandParserTest {

    private LineCommandParser parser = new LineCommandParser();

    @Test
    public void shouldDetectApplicableLine() {
        assertTrue(parser.isApplicableTo("LINE 123"));
    }

    @Test
    public void shouldParseCorrectLineCommand() {
        Assert.assertEquals(new LineCommand(123), parser.parse("LINE 123"));
    }

    @Test
    public void shouldParseBigLineNumber() {
        assertEquals(new LineCommand(123456789123L), parser.parse("LINE 123456789123"));
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForMissingLineNr() {
        parser.parse("LINE");
    }

    @Test(expected = CommandParserException.class)
    public void shouldThrowExceptionForInvalidLineNr() {
        parser.parse("LINE a12a");
    }

}