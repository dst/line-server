package com.stefanski.liner.file;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.liner.index.IndexException;
import com.stefanski.liner.index.LineMetadata;
import com.stefanski.liner.index.TextFileIndex;
import com.stefanski.liner.util.SeekableByteChannelReader;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

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

        void addLine(String line) throws IOException {
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
    public void shouldReturnTextForCorrectLineNr() {
        for (int i = 1; i <= 4; i++) {
            Assert.assertEquals("line" + i, textFile.getLine(i));
        }
    }

    @Test(expected = TextFileException.class)
    public void shouldThrowExceptionForNegativeLineNr() {
        textFile.getLine(-1);
    }

    @Test(expected = TextFileException.class)
    public void shouldThrowExceptionForZeroLineNr() {
        textFile.getLine(0);
    }

    @Test(expected = TextFileException.class)
    public void shouldThrowExceptionForTooBigLineNr() {
        textFile.getLine(5);
    }

}
