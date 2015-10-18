package com.stefanski.liner.server.cmd;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Dariusz Stefanski
 * @date 18 Oct 2015
 */
public class QuitCommandParserTest {

    private QuitCommandParser parser = new QuitCommandParser();

    @Test
    public void shouldDetectApplicableLine() {
        assertTrue(parser.isApplicableTo("QUIT"));
    }

    @Test
    public void shouldParseQuitCommand() {
        assertEquals(QuitCommand.getInstance(), parser.parse("QUIT"));
    }

}