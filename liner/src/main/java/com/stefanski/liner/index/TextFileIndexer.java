package com.stefanski.liner.index;

import static com.stefanski.liner.LinerConstants.HDD_MB;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.stefanski.liner.util.FileProcessingProgressMonitor;
import com.stefanski.liner.util.SeekableByteChannelReader;
import com.stefanski.liner.util.StdLogger;

/**
 * Builds an index.
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
        LineOffsetFinder lineFinder = new LineOffsetFinder(fileFC);

        progressMonitor.start();
        while (lineFinder.hasNextLine()) {
            long pos = lineFinder.getNextLineOffset();
            indexBuf.putLong(pos);
            lineCount++;

            progressMonitor.processedLine(pos);

            // Write only full buffer
            if (!indexBuf.hasRemaining()) {
                writeIndexBuf(indexBuf);
            }
        }

        // To calculate later length of last line
        indexBuf.putLong(fileSize);

        // Write rest of index buffer
        writeIndexBuf(indexBuf);

        progressMonitor.stop();

        SeekableByteChannelReader indexReader = new SeekableByteChannelReader(indexFC);
        return new OffsetIndex(indexReader, lineCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        fileFC.close();
        // Don't close index channel because it is passed to index
    }

    private void writeIndexBuf(ByteBuffer buf) throws IOException {
        // Write from buffer's begin
        buf.flip();

        indexFC.write(buf);

        // Prepare for putting in next iteration
        buf.flip();
    }

}