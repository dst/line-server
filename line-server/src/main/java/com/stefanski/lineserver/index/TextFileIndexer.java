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

import com.stefanski.lineserver.StdLogger;

/**
 * Creates index in a file for the specified text file.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class TextFileIndexer {

    // position + length in bytes
    static final int INDEX_LINE_METADATA_LEN = (Long.SIZE + Integer.SIZE) / 8;

    private static final long MB = 1024 * 1024;
    private static final long PROCESSED_CHUNK_SIZE_TO_REPORT_IN_MB = 100;
    private static final long PROCESSED_CHUNK_SIZE_TO_REPORT = PROCESSED_CHUNK_SIZE_TO_REPORT_IN_MB
            * MB;

    /**
     * 
     * @param filePath
     *            A path to the text file.
     * @return
     * @throws IOException
     */
    // TODO(dst), Sep 3, 2013: Impl in more efficient fashion. Things to optimize:
    // - we don't need read lines to String. We can read whole blocks and search only \n chars.
    // - Maybe FileChannel.map usage
    // - reimplement writing in more efficient way
    public static TextFileIndex buildIndex(Path filePath) throws IOException {
        StdLogger.info("Indexing file...");
        long start = System.currentTimeMillis();

        Path indexPath = createIndexFile(filePath);

        long position = 0;
        long lineCount = 0;
        int chunkCount = 1;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.US_ASCII);
                FileChannel writer = FileChannel.open(indexPath, WRITE)) {

            ByteBuffer buf = ByteBuffer.allocate(INDEX_LINE_METADATA_LEN);
            String line;

            while ((line = reader.readLine()) != null) {
                buf.putLong(position);
                buf.putInt(line.length());
                // write from buffer's begin
                buf.flip();
                writer.write(buf);
                // prepare for putting in next iteration
                buf.flip();

                position += line.length() + 1; // 1 for \n
                lineCount++;

                // log progress
                if (position > PROCESSED_CHUNK_SIZE_TO_REPORT * chunkCount) {
                    long elapsedTime = System.currentTimeMillis() - start;
                    StdLogger.info(String.format("Processed %s MB. Current processing time: %s",
                            PROCESSED_CHUNK_SIZE_TO_REPORT_IN_MB, elapsedTime));
                    chunkCount++;
                }
            }
        }

        // log processing stats
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
