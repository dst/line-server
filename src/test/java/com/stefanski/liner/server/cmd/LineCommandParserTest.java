package com.stefanski.liner.server.cmd;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Dariusz Stefanski
 * @date 18 Oct 2015
 */
public class LineCommandParserTest {

    private LineCommandParser parser = new LineCommandParser();

    @Test
    public void shouldDetectApplicableLine() {
        assertTrue(parser.isApplicableTo("LINE 123"));
    }

    @Test
    public void shouldParseCorrectLineCommand() {
        assertEquals(new LineCommand(123), parser.parse("LINE 123"));
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