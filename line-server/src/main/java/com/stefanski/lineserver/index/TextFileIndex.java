package com.stefanski.lineserver.index;

import static com.stefanski.lineserver.index.TextFileIndexer.INDEX_LINE_METADATA_LEN;
import static java.nio.file.StandardOpenOption.READ;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * The index to file.
 * 
 * It is not thread-safe.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class TextFileIndex {
    private final long lineCount;

    /**
     * FileChannel for a random access to an index file.
     */
    private final FileChannel fileChannel;

    public TextFileIndex(Path indexPath, long lineCount) throws IOException {
        this.lineCount = lineCount;
        fileChannel = FileChannel.open(indexPath, READ);
    }

    /**
     * @param lineNr
     * @return
     * @throws IndexException
     */
    public LineMetadata getLineMetadata(long lineNr) throws IndexException {
        try {
            // Set starting position in index file. The first line of the file is line 1.
            long pos = (lineNr - 1) * INDEX_LINE_METADATA_LEN;
            fileChannel.position(pos);

            // and read metadata of specified line
            ByteBuffer buf = ByteBuffer.allocate(INDEX_LINE_METADATA_LEN);
            int readCount;
            do {
                readCount = fileChannel.read(buf);
            } while (readCount != -1 && buf.hasRemaining());

            if (buf.hasRemaining()) {
                throw new IndexException("Buffer with line metadata not filled");
            }

            buf.flip();
            return new LineMetadata(buf.getLong(), buf.getInt());

        } catch (IOException e) {
            throw new IndexException("I/O exception during reading index", e);
        }
    }

    /**
     * @return
     */
    public long getLineCount() {
        return lineCount;
    }
}
