package com.stefanski.lineserver.index;

import static java.nio.file.StandardOpenOption.WRITE;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.stefanski.lineserver.util.StdLogger;

/**
 * Builds index.
 *
 * It should be done as fast as possible because it delays starting server.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class TextFileIndexer {

    // position + length in bytes
    static final int INDEX_LINE_METADATA_LEN = (Long.SIZE + Integer.SIZE) / 8;

    private static final int WRITER_BUFFER_SIZE = INDEX_LINE_METADATA_LEN * 1024 * 1024;

    private static final long MB = 1024 * 1024;
    private static final long PROCESSED_CHUNK_SIZE_TO_REPORT_IN_MB = 100;
    private static final long PROCESSED_CHUNK_SIZE_TO_REPORT = PROCESSED_CHUNK_SIZE_TO_REPORT_IN_MB
            * MB;

    private TextFileIndexer() {
    }

    /**
     * Builds an index as a binary file with offsets to each line of the original file. The index
     * size is 12 B * line count.
     * 
     * @param filePath
     *            A path to the text file.
     * @return
     * @throws IOException
     */
    // TODO(dst), Sep 3, 2013: Impl in more efficient fashion. Things to optimize:
    // - we don't need read lines to String. We can read whole blocks and search only \n chars.
    // - Maybe FileChannel.map usage
    public static TextFileIndex buildIndex(Path filePath) throws IOException {
        StdLogger.info("Indexing file...");
        long start = System.currentTimeMillis();

        Path indexPath = createIndexFile(filePath);

        long position = 0;
        long lineCount = 0;
        int chunkCount = 1;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.US_ASCII);
                FileChannel writer = FileChannel.open(indexPath, WRITE)) {

            ByteBuffer buf = ByteBuffer.allocate(WRITER_BUFFER_SIZE);
            String line;

            while ((line = reader.readLine()) != null) {
                buf.putLong(position);
                buf.putInt(line.length());

                // Write only full buffer
                if (!buf.hasRemaining()) {
                    // Write from buffer's begin
                    buf.flip();
                    writer.write(buf);
                    // Prepare for putting in next iteration
                    buf.flip();
                }

                position += line.length() + 1; // 1 for \n
                lineCount++;

                // Log progress
                if (position > PROCESSED_CHUNK_SIZE_TO_REPORT * chunkCount) {
                    long elapsedTime = System.currentTimeMillis() - start;
                    StdLogger.info(String.format("Processed %s MB. Current processing time: %s",
                            PROCESSED_CHUNK_SIZE_TO_REPORT_IN_MB, elapsedTime));
                    chunkCount++;
                }
            }

            // Write rest of buffer
            buf.flip();
            writer.write(buf);
        }

        // Log processing stats
        long elapsedTime = System.currentTimeMillis() - start;
        StdLogger.info(String.format("Index build in %s ms", elapsedTime));
        StdLogger.info(String.format("Lines: %d, size: %d MB", lineCount, position / MB));

        return new TextFileIndex(indexPath, lineCount);
    }

    private static Path createIndexFile(Path filePath) throws IOException {
        String indexFileName = filePath.toString() + "_lineServerIndex";
        Path indexPath = FileSystems.getDefault().getPath(indexFileName);

        // Delete if exists
        if (Files.exists(indexPath)) {
            StdLogger.info(String.format("Index file ('%s') exists. Removing", indexPath));
            Files.delete(indexPath);
        }

        // and create
        indexPath = Files.createFile(indexPath);

        return indexPath;
    }
}
