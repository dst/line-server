package com.stefanski.lineserver.index;

import static com.stefanski.lineserver.LineServerConstants.HDD_MB;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.stefanski.lineserver.util.FileProcessingProgressMonitor;
import com.stefanski.lineserver.util.SeekableByteChannelReader;
import com.stefanski.lineserver.util.StdLogger;

/**
 * Builds index.
 * 
 * It should be done as fast as possible because it delays starting server.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class TextFileIndexer implements AutoCloseable {

    // offset has type long
    static final int OFFSET_SIZE = Long.SIZE / 8;

    private static final int INDEX_BUF_SIZE = 20 * OFFSET_SIZE * HDD_MB;

    /**
     * A channel to an indexed file.
     */
    private final FileChannel fileFC;

    /**
     * A channel to an index file
     */
    private final FileChannel indexFC;

    /**
     * Reads file using fileFC and write index using indexFC.
     * 
     * @param fileFC
     * @param indexFC
     */
    public TextFileIndexer(FileChannel fileFC, FileChannel indexFC) {
        this.fileFC = fileFC;
        this.indexFC = indexFC;
    }

    /**
     * Builds an index as a binary file with offsets to each line of the original file. The index
     * size is 8B * line count.
     * 
     * @return
     * @throws IOException
     */
    public TextFileIndex buildIndex() throws IOException {
        StdLogger.info("Indexing file...");

        long fileSize = fileFC.size();
        FileProcessingProgressMonitor progressMonitor = new FileProcessingProgressMonitor(fileSize);

        long lineCount = 0;

        ByteBuffer indexBuf = ByteBuffer.allocate(INDEX_BUF_SIZE);

        // TODO(dst), Sep 19, 2013: move to constructor
        LineOffsetFinder lineFinder = new LineOffsetFinder(fileFC);

        progressMonitor.start();
        while (lineFinder.hasNextLine()) {
            long pos = lineFinder.getNextLineOffset();
            indexBuf.putLong(pos);
            lineCount++;

            progressMonitor.processedLine(pos);

            // Write only full buffer
            if (!indexBuf.hasRemaining()) {
                // Write from buffer's begin
                indexBuf.flip();
                indexFC.write(indexBuf);
                // Prepare for putting in next iteration
                indexBuf.flip();
            }
        }

        // To calculate later length of last line
        indexBuf.putLong(fileSize);

        // Write rest of index buffer
        indexBuf.flip();
        indexFC.write(indexBuf);

        progressMonitor.stop();

        SeekableByteChannelReader indexReader = new SeekableByteChannelReader(indexFC);
        return new OffsetIndex(indexReader, lineCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // TODO(dst), Sep 19, 2013: call it
            public
            void close() throws Exception {
        fileFC.close();
        // Don't close index channel because it is passed to index
    }
}
