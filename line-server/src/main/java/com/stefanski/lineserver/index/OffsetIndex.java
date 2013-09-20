package com.stefanski.lineserver.index;

import static com.stefanski.lineserver.index.TextFileIndexer.OFFSET_SIZE;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.stefanski.lineserver.util.SeekableByteChannelReader;

/**
 * The index is kept in a binary file with offsets to lines.
 * 
 * It is not thread-safe.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
// TODO(dst), Sep 19, 2013: move this package to file package
public class OffsetIndex implements TextFileIndex {
    /**
     * A line count in the indexed file (not index).
     */
    private final long lineCount;

    /**
     * A reader for a random access to an index file.
     */
    private final SeekableByteChannelReader reader;

    public OffsetIndex(SeekableByteChannelReader reader, long lineCount) throws IOException {
        this.reader = reader;
        this.lineCount = lineCount;
    }

    /**
     * {@inheritDoc}
     */
    public LineMetadata getLineMetadata(long lineNr) throws IndexException {
        try {
            ByteBuffer buf = readOffsets(lineNr);
            return offsets2metadata(buf);
        } catch (IOException e) {
            throw new IndexException("I/O exception during reading index", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getLineCount() {
        return lineCount;
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
