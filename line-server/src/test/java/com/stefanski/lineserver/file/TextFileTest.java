package com.stefanski.lineserver.file;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.lineserver.index.IndexException;
import com.stefanski.lineserver.index.LineMetadata;
import com.stefanski.lineserver.index.TextFileIndex;
import com.stefanski.lineserver.util.SeekableByteChannelReader;

/**
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 */
public class TextFileTest {

    //
    // line1
    // line2
    // line3
    // line4
    private static IndexedTextFile textFile;

    @BeforeClass
    public static void setUp() throws Exception {
        MockBuilder builder = new MockBuilder();
        builder.addLine("line1");
        builder.addLine("line2");
        builder.addLine("line3");
        builder.addLine("line4");

        textFile = new IndexedTextFile(builder.getReaderMock(), builder.getIndexMock());
    }

    static class MockBuilder {

        private long lineNr;
        private long offset;

        private final SeekableByteChannelReader reader;
        private final TextFileIndex index;

        MockBuilder() {
            lineNr = 1;
            offset = 0;

            index = Mockito.mock(TextFileIndex.class);
            reader = Mockito.mock(SeekableByteChannelReader.class);
        }

        void addLine(String line) throws IndexException, IOException {
            int len = line.length();
            when(index.getLineMetadata(lineNr)).thenReturn(new LineMetadata(offset, len));
            when(reader.read(offset, len)).thenReturn(ByteBuffer.wrap(line.getBytes()));
            offset += len + 1; // 1 for \n
            lineNr++;
        }

        TextFileIndex getIndexMock() {
            when(index.getLineCount()).thenReturn(lineNr - 1);
            return index;
        }

        SeekableByteChannelReader getReaderMock() {
            return reader;
        }
    }

    @Test
    public void shouldReturnTextForCorrectLineNr() throws TextFileException {
        for (int i = 1; i <= 4; i++) {
            Assert.assertEquals("line" + i, textFile.getLine(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNegativeLineNr() throws TextFileException {
        textFile.getLine(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForZeroLineNr() throws TextFileException {
        textFile.getLine(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForTooBigLineNr() throws TextFileException {
        textFile.getLine(5);
    }

    @Test
    public void shouldDetectValidLinesNr() {
        List<Integer> validLineNrs = Arrays.asList(1, 2, 3, 4);
        for (int lineNr : validLineNrs) {
            Assert.assertTrue(textFile.isLineNrValid(lineNr));
        }
    }

    @Test
    public void shouldDetectInvalidLinesNr() {
        List<Integer> invalidLineNrs = Arrays.asList(-5, -1, 0, 5, 10);
        for (int lineNr : invalidLineNrs) {
            Assert.assertFalse(textFile.isLineNrValid(lineNr));
        }
    }
}
