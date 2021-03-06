package com.stefanski.liner.server.command;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.response.LineResponse;
import com.stefanski.liner.server.response.Response;

import static org.mockito.Mockito.when;

/**
 * @author Dariusz Stefanski
 * @since Sep 21, 2013
 */
public class LineCommandTest {

    @Test
    public void shouldReturnErrorForInvalidLineNr() {
        long lineNr = 123L;
        LineCommand cmd = new LineCommand(lineNr);

        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.getLine(lineNr)).thenThrow(TextFileException.class);
        CommandContext ctx = new CommandContext(null, textFile);

        Response resp = cmd.execute(ctx);
        assertIsErrResp(resp);
    }

    @Test
    public void shouldReturnLineForValidLineNr() {
        long lineNr = 123L;
        String line = "Very nice line";
        LineCommand cmd = new LineCommand(lineNr);

        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.getLine(lineNr)).thenReturn(line);
        CommandContext ctx = new CommandContext(null, textFile);

        Response resp = cmd.execute(ctx);
        Assert.assertEquals(LineResponse.ok(line), resp);
    }

    private void assertIsErrResp(Response resp) {
        Assert.assertEquals(LineResponse.error(), resp);
    }
}
