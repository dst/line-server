package com.stefanski.lineserver.index;

import static com.stefanski.lineserver.index.TextFileIndexer.OFFSET_SIZE;
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
     * Gets a metadata of specified line from the index.
     * 
     * @param lineNr
     * @return
     * @throws IndexException
     */
    public LineMetadata getLineMetadata(long lineNr) throws IndexException {
        try {
            // Set starting position in index file. The first line of the file is line 1.
            long pos = (lineNr - 1) * OFFSET_SIZE;
            fileChannel.position(pos);

            // Read 2 offsets
            ByteBuffer buf = ByteBuffer.allocate(OFFSET_SIZE * 2);
            int readCount;
            do {
                readCount = fileChannel.read(buf);
            } while (readCount != -1 && buf.hasRemaining());

            if (buf.hasRemaining()) {
                throw new IndexException("Buffer with line metadata not filled");
            }

            buf.flip();
            long offset = buf.getLong();
            long nextOffset = buf.getLong();

            int lineLen = (int) (nextOffset - offset - 1);
            assert lineLen >= 0;

            return new LineMetadata(offset, lineLen);

        } catch (IOException e) {
            throw new IndexException("I/O exception during reading index", e);
        }
    }

    /**
     * @return Line number in file which was indexed
     */
    public long getLineCount() {
        return lineCount;
    }
}
