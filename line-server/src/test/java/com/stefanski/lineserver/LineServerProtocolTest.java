package com.stefanski.lineserver;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

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

        Assert.assertEquals("ERR\r\n", protocol.processGetCmd("GET 1"));
    }

    @Test
    public void shouldReturnLineForValidLineNr() throws LineServerException {
        TextFile textFile = Mockito.mock(TextFile.class);
        when(textFile.isLineNrValid(Mockito.anyInt())).thenReturn(true);
        when(textFile.getLine(Mockito.anyInt())).thenReturn("Very nice line");
        LineServerProtocol protocol = new LineServerProtocol(textFile);

        Assert.assertEquals("OK\r\nVery nice line", protocol.processGetCmd("GET 1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidFormatOfGetCmd() {
        createProtocolWithMockedTextFile().processGetCmd("GET");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNotParsableLineNr() {
        createProtocolWithMockedTextFile().processGetCmd("GET a12a");
    }

    private LineServerProtocol createProtocolWithMockedTextFile() {
        TextFile textFile = Mockito.mock(TextFile.class);
        return new LineServerProtocol(textFile);
    }
}
