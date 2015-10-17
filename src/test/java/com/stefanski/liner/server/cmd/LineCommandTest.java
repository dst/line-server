package com.stefanski.liner.server.cmd;

import static org.mockito.Mockito.when;

import com.stefanski.liner.server.resp.LineResponse;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.resp.Response;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class LineCommandTest {

    @Test
    public void shouldReturnErrorForInvalidLineNr() {
        long lineNr = 123L;
        LineCommand cmd = new LineCommand(lineNr);

        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.isLineNrValid(lineNr)).thenReturn(false);
        CommandContext ctx = new CommandContext(null, textFile);

        Response resp = cmd.execute(ctx);
        assertIsErrResp(resp);
    }

    @Test
    public void shouldReturnLineForValidLineNr() throws TextFileException {
        long lineNr = 123L;
        String line = "Very nice line";
        LineCommand cmd = new LineCommand(lineNr);

        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.isLineNrValid(lineNr)).thenReturn(true);
        when(textFile.getLine(lineNr)).thenReturn(line);
        CommandContext ctx = new CommandContext(null, textFile);

        Response resp = cmd.execute(ctx);
        Assert.assertEquals(LineResponse.createOkResp(line), resp);
    }

    private void assertIsErrResp(Response resp) {
        Assert.assertEquals(LineResponse.createErrResp(), resp);
    }
}
