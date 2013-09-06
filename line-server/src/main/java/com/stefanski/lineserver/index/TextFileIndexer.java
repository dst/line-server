package com.stefanski.lineserver.index;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
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

    // offset has type long
    static final int OFFSET_SIZE = Long.SIZE / 8;

    private static final int HDD_MB = 1_000_000;
    private static final int FILE_BUF_SIZE = 256 * HDD_MB;
    private static final int INDEX_BUF_SIZE = 20 * OFFSET_SIZE * HDD_MB;

    /**
     * The path to file which is indexed.
     */
    private final Path filePath;

    public static TextFileIndexer createIndexer(Path filePath) {
        return new TextFileIndexer(filePath);
    }

    /**
     * 
     * @param filePath
     */
    public TextFileIndexer(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Builds an index as a binary file with offsets to each line of the original file. The index
     * size is 8B * line count.
     * 
     * @param filePath
     *            A path to the text file.
     * @return
     * @throws IOException
     */
    public TextFileIndex buildIndex() throws IOException {
        StdLogger.info("Indexing file...");
        long start = System.currentTimeMillis();

        Path indexPath = createIndexFile(filePath);

        long lineCount = 0;
        long size = 0;

        try (FileChannel fileFC = FileChannel.open(filePath, READ);
                FileChannel indexFC = FileChannel.open(indexPath, WRITE)) {

            ByteBuffer indexBuf = ByteBuffer.allocate(INDEX_BUF_SIZE);
            byte[] fileArr = new byte[FILE_BUF_SIZE];

            size = fileFC.size();
            logFileSize(size);

            long lastPos = 0;
            long processed = 0;
            int lineLength;

            while (processed < size) {
                long bytesToProcess = size - processed;
                long bytesToMap = Math.min(bytesToProcess, FILE_BUF_SIZE);
                assert bytesToMap <= Integer.MAX_VALUE;

                MappedByteBuffer mappedBuf = fileFC.map(MapMode.READ_ONLY, processed, bytesToMap);

                mappedBuf.get(fileArr, 0, (int) bytesToMap);
                for (int i = 0; i < bytesToMap; i++) {
                    if (fileArr[i] == '\n') {
                        lineLength = (int) (processed + i - lastPos);
                        assert lineLength >= 0;

                        indexBuf.putLong(lastPos);

                        lineCount++;
                        lastPos += lineLength + 1;

                        // Write only full buffer
                        if (!indexBuf.hasRemaining()) {
                            // Write from buffer's begin
                            indexBuf.flip();
                            indexFC.write(indexBuf);
                            // Prepare for putting in next iteration
                            indexBuf.flip();
                        }
                    }
                }

                processed += bytesToMap;
                logProgress(start, processed, size);
            }

            // To calculate later length of last line
            indexBuf.putLong(lastPos);

            // Write rest of index buffer
            indexBuf.flip();
            indexFC.write(indexBuf);
        }

        logProcessingStats(start, lineCount, size);

        return new TextFileIndex(indexPath, lineCount);
    }

    private Path createIndexFile(Path filePath) throws IOException {
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

    private long byte2MB(long b) {
        return b / HDD_MB;
    }

    private void logFileSize(long size) {
        StdLogger.info(String.format("File size to process: %s MB", byte2MB(size)));
    }

    private void logProgress(long start, long done, long size) {
        long elapsedTime = System.currentTimeMillis() - start;
        long percent = (100 * done) / size;
        StdLogger.info(String.format("Processed %d MB in %d (%d %%)", byte2MB(done), elapsedTime,
                percent));
    }

    private void logProcessingStats(long start, long lineCount, long size) {
        long elapsedTime = System.currentTimeMillis() - start;

        StdLogger.info(String.format("Index build in %s ms", elapsedTime));
        StdLogger.info(String.format("Lines: %d, size: %d MB", lineCount, size / HDD_MB));
    }
}
