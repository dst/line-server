package com.stefanski.lineserver.server;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.lineserver.server.cmd.GetCommand;
import com.stefanski.lineserver.server.resp.GetResponse;
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

        GetResponse resp = protocol.processGetCmd(new GetCommand(1));
        assertIsErrResp(resp);
    }

    @Test
    public void shouldReturnLineForValidLineNr() throws TextFileException {
        String line = "Very nice line";
        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.isLineNrValid(Mockito.anyInt())).thenReturn(true);
        when(textFile.getLine(Mockito.anyInt())).thenReturn(line);
        LineServerProtocol protocol = new LineServerProtocol(textFile);

        GetResponse resp = protocol.processGetCmd(new GetCommand(1));
        Assert.assertEquals(GetResponse.createOkResp(line), resp);
    }

    // TODO(dst), Sep 12, 2013: logic moved to parser
    // @Test
    // public void shouldReturnErrorForInvalidFormatOfGetCmd() {
    // GetResponse resp = createProtocolWithMockedTextFile().processGetCmd("GET");
    // assertIsErrResp(resp);
    // }
    //
    // @Test
    // public void shouldReturnErrorForNotParsableLineNr() {
    // GetResponse resp = createProtocolWithMockedTextFile().processGetCmd("GET a12a");
    // assertIsErrResp(resp);
    // }

    private void assertIsErrResp(GetResponse resp) {
        Assert.assertEquals(GetResponse.createErrResp(), resp);
    }

    private LineServerProtocol createProtocolWithMockedTextFile() {
        TextFile textFile = Mockito.mock(TextFile.class);
        return new LineServerProtocol(textFile);
    }
}
