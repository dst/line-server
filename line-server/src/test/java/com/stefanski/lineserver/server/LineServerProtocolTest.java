package com.stefanski.lineserver.server;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.lineserver.util.TextFile;
import com.stefanski.lineserver.util.TextFileException;

/**
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class LineServerProtocolTest {

    @Test
    public void shouldReturnErrorForInvalidLineNr() throws LineServerException {
        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.isLineNrValid(Mockito.anyInt())).thenReturn(false);
        LineServerProtocol protocol = new LineServerProtocol(textFile);

        Response resp = protocol.processGetCmd("GET 1");
        assertIsErrResp(resp);
    }

    @Test
    public void shouldReturnLineForValidLineNr() throws TextFileException {
        String line = "Very nice line";
        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.isLineNrValid(Mockito.anyInt())).thenReturn(true);
        when(textFile.getLine(Mockito.anyInt())).thenReturn(line);
        LineServerProtocol protocol = new LineServerProtocol(textFile);

        Response resp = protocol.processGetCmd("GET 1");
        Assert.assertEquals(Response.createOkResp(line), resp);
    }

    @Test
    public void shouldReturnErrorForInvalidFormatOfGetCmd() {
        Response resp = createProtocolWithMockedTextFile().processGetCmd("GET");
        assertIsErrResp(resp);
    }

    @Test
    public void shouldReturnErrorForNotParsableLineNr() {
        Response resp = createProtocolWithMockedTextFile().processGetCmd("GET a12a");
        assertIsErrResp(resp);
    }

    private void assertIsErrResp(Response resp) {
        Assert.assertEquals(Response.createErrResp(), resp);
    }

    private LineServerProtocol createProtocolWithMockedTextFile() {
        TextFile textFile = Mockito.mock(TextFile.class);
        return new LineServerProtocol(textFile);
    }
}
