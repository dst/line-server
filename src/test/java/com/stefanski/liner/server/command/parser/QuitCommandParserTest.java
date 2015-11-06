package com.stefanski.liner.server.command.parser;

import junit.framework.Assert;
import org.junit.Test;

import com.stefanski.liner.server.command.QuitCommand;
import com.stefanski.liner.server.command.parser.QuitCommandParser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Dariusz Stefanski
 * @since 18 Oct 2015
 */
public class QuitCommandParserTest {

    private QuitCommandParser parser = new QuitCommandParser();

    @Test
    public void shouldDetectApplicableLine() {
        assertTrue(parser.isApplicableTo("QUIT"));
    }

    @Test
    public void shouldParseQuitCommand() {
        Assert.assertEquals(QuitCommand.getInstance(), parser.parse("QUIT"));
    }

}