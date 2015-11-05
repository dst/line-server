package com.stefanski.liner.server.cmd;

import org.junit.Test;

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
        assertEquals(ShutdownCommand.getInstance(), parser.parse("SHUTDOWN"));
    }

}