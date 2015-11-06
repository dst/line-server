package com.stefanski.liner.server.command.parser;

import junit.framework.Assert;
import org.junit.Test;

import com.stefanski.liner.server.command.ShutdownCommand;
import com.stefanski.liner.server.command.parser.ShutdownCommandParser;

import static junit.framework.Assert.assertEquals;

/**
 * @author Dariusz Stefanski
 * @since 18 Oct 2015
 */
public class ShutdownCommandParserTest {

    private ShutdownCommandParser parser = new ShutdownCommandParser();

    @Test
    public void shouldDetectApplicableLine() throws Exception {
        parser.isApplicableTo("SHUTDOWN");
    }

    @Test
    public void shouldParseShutdownCommand() {
        Assert.assertEquals(ShutdownCommand.getInstance(), parser.parse("SHUTDOWN"));
    }

}