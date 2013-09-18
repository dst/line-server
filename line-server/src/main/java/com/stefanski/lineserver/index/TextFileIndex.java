package com.stefanski.lineserver.index;

import static com.stefanski.lineserver.index.TextFileIndexer.OFFSET_SIZE;
import static java.nio.file.StandardOpenOption.READ;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;

/**
 * The index of file. The index is kept in a binary file with offsets to lines.
 * 
 * It is not thread-safe.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class TextFileIndex {
    /**
     * A line count in the indexed file (not index).
     */
    private final long lineCount;

    /**
     * A channel for a random access to an index file.
     */
    private final SeekableByteChannel fileChannel;

    // TODO(dst), Sep 16, 2013: fileChannel to contructor?
    public TextFileIndex(Path indexPath, long lineCount) throws IOException {
        this.lineCount = lineCount;
        fileChannel = FileChannel.open(indexPath, READ);
    }

    /**
     * Gets a metadata of specified line from the index.
     * 
     * @param lineNr
     * @return
     * @throws IndexException
     */
    public LineMetadata getLineMetadata(long lineNr) throws IndexException {
        try {
            seekToMetadataOfLine(lineNr);
            ByteBuffer buf = readOffsets();
            return offsets2metadata(buf);
        } catch (IOException e) {
            throw new IndexException("I/O exception during reading index", e);
        }
    }

    /**
     * @return Line count in a file which was indexed
     */
    public long getLineCount() {
        return lineCount;
    }

    private void seekToMetadataOfLine(long lineNr) throws IOException {
        // Set starting position in index file. The first line of the file is line 1.
        long pos = (lineNr - 1) * OFFSET_SIZE;
        fileChannel.position(pos);
    }

    private ByteBuffer readOffsets() throws IOException, IndexException {
        // Read 2 offsets
        ByteBuffer buf = ByteBuffer.allocate(OFFSET_SIZE * 2);
        fillBuffer(buf);
        return buf;
    }

    private void fillBuffer(ByteBuffer buf) throws IOException, IndexException {
        int readCount;
        do {
            readCount = fileChannel.read(buf);
        } while (readCount != -1 && buf.hasRemaining());

        if (buf.hasRemaining()) {
            throw new IndexException("Buffer with line metadata not filled");
        }
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
