package com.stefanski.lineserver.server;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.lineserver.file.IndexedTextFile;
import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.file.TextFileException;
import com.stefanski.lineserver.server.cmd.GetCommand;
import com.stefanski.lineserver.server.resp.GetResponse;

/**
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class LineServerProtocolTest {

    @Test
    public void shouldReturnErrorForInvalidLineNr() throws LineServerException {
        IndexedTextFile textFile = Mockito.mock(IndexedTextFile.class);
        when(textFile.isLineNrValid(Mockito.anyInt())).thenReturn(false);
        LineServerProtocol protocol = new LineServerProtocol(textFile);

        GetResponse resp = protocol.processGetCmd(new GetCommand(1));
        assertIsErrResp(resp);
    }

    @Test
    public void shouldReturnLineForValidLineNr() throws TextFileException {
        String line = "Very nice line";
        IndexedTextFile textFile = Mockito.mock(IndexedTextFile.class);
        when(textFile.isLineNrValid(Mockito.anyInt())).thenReturn(true);
        when(textFile.getLine(Mockito.anyInt())).thenReturn(line);
        LineServerProtocol protocol = new LineServerProtocol(textFile);

        GetResponse resp = protocol.processGetCmd(new GetCommand(1));
        Assert.assertEquals(GetResponse.createOkResp(line), resp);
    }

    private void assertIsErrResp(GetResponse resp) {
        Assert.assertEquals(GetResponse.createErrResp(), resp);
    }

    private LineServerProtocol createProtocolWithMockedTextFile() {
        TextFile textFile = Mockito.mock(TextFile.class);
        return new LineServerProtocol(textFile);
    }
}
