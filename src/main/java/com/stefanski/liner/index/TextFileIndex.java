package com.stefanski.liner.index;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.stefanski.liner.util.SeekableByteChannelReader;

import static com.stefanski.liner.index.TextFileIndexer.OFFSET_SIZE;

/**
 * The index is kept in a binary file with offsets to lines.
 * 
 * It is not thread-safe.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class TextFileIndex implements AutoCloseable {

    /**
     * A reader for a random access to an index file.
     */
    private final SeekableByteChannelReader reader;

    /**
     * A line count in the indexed file (not index).
     */
    private final long lineCount;

    public TextFileIndex(SeekableByteChannelReader reader, long lineCount) throws IOException {
        this.reader = reader;
        this.lineCount = lineCount;
    }

    /**
     * Gets a metadata of a specified line from the index.
     *
     * @param lineNr
     * @return
     * @throws IndexException
     */
    public LineMetadata getLineMetadata(long lineNr) {
        try {
            ByteBuffer buf = readOffsets(lineNr);
            return offsets2metadata(buf);
        } catch (IOException e) {
            throw new IndexException("I/O exception during reading index", e);
        }
    }

    /**
     * @return A line count in a file which was indexed
     */
    public long getLineCount() {
        return lineCount;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    private ByteBuffer readOffsets(long lineNr) throws IOException {
        return reader.read(getStartingPosOfLineMetadata(lineNr), OFFSET_SIZE * 2);
    }

    private long getStartingPosOfLineMetadata(long lineNr) {
        // The first line of the file is line 1.
        return (lineNr - 1) * OFFSET_SIZE;
    }

    private LineMetadata offsets2metadata(ByteBuffer buf) {
        // Prepare a buffer for reading
        buf.flip();

        long offset = buf.getLong();
        long nextOffset = buf.getLong();

        int lineLen = (int) (nextOffset - offset - 1);
        assert lineLen >= 0;

        return new LineMetadata(offset, lineLen);
    }
}
