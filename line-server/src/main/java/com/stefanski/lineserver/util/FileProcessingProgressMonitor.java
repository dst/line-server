package com.stefanski.lineserver.util;

import static com.stefanski.lineserver.LineServerConstants.HDD_MB;

/**
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
public class FileProcessingProgressMonitor {
    private static final long CHUNK_SIZE = 100 * HDD_MB;

    private final long size;
    private long start;
    private long processedLines = 0;
    private long processedBytes = 0;
    private int chunkNr = 1;

    public FileProcessingProgressMonitor(long size) {
        this.size = size;
        logFileSize();
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        logProcessingStats();
    }

    public void processedLine(long startPos) {
        processedLines++;
        processedBytes = startPos;

        if (processedBytes > CHUNK_SIZE * chunkNr) {
            logProgress();
            chunkNr++;
        }
    }

    private void logFileSize() {
        StdLogger.info(String.format("File size to process: %s MB", byte2MB(size)));
    }

    private void logProgress() {
        long elapsedTime = System.currentTimeMillis() - start;
        long percent = (100 * processedBytes) / size;
        StdLogger.info(String.format("Processed %d MB in %d ms (%d %%)", byte2MB(processedBytes),
                elapsedTime, percent));
    }

    private void logProcessingStats() {
        long elapsedTime = System.currentTimeMillis() - start;

        StdLogger.info(String.format("Index build in %s ms", elapsedTime));
        StdLogger.info(String.format("Lines: %d, size: %d MB", processedLines, size / HDD_MB));
    }

    private long byte2MB(long b) {
        return b / HDD_MB;
    }
}
