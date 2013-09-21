package com.stefanski.lineserver.server.cmd;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.file.TextFileException;
import com.stefanski.lineserver.server.LineServerException;
import com.stefanski.lineserver.server.resp.GetResponse;
import com.stefanski.lineserver.server.resp.Response;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class GetCommandTest {

    @Test
    public void shouldReturnErrorForInvalidLineNr() throws LineServerException {
        long lineNr = 123L;
        GetCommand cmd = new GetCommand(lineNr);

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
        GetCommand cmd = new GetCommand(lineNr);

        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.isLineNrValid(lineNr)).thenReturn(true);
        when(textFile.getLine(lineNr)).thenReturn(line);
        CommandContext ctx = new CommandContext(null, textFile);

        Response resp = cmd.execute(ctx);
        Assert.assertEquals(GetResponse.createOkResp(line), resp);
    }

    private void assertIsErrResp(Response resp) {
        Assert.assertEquals(GetResponse.createErrResp(), resp);
    }
}
