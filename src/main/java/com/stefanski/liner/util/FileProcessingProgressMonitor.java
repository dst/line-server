package com.stefanski.liner.util;

import lombok.extern.slf4j.Slf4j;

import static com.stefanski.liner.LinerConstants.HDD_MB;

/**
 * Reports progress of file processing.
 * 
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
@Slf4j
public class FileProcessingProgressMonitor {
    private static final long CHUNK_SIZE = 100 * HDD_MB;

    private final long size;
    private long start;
    private long processedLines = 0;
    private long processedBytes = 0;
    private int chunkNr = 1;

    /**
     * 
     * @param size
     *            A size of a processed file
     */
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

    /**
     * This method should be called after processing each line.
     * 
     * @param startPos
     *            A starting position of a processed line in file
     */
    public void processedLine(long startPos) {
        processedLines++;
        processedBytes = startPos;

        if (processedBytes > CHUNK_SIZE * chunkNr) {
            logProgress();
            chunkNr++;
        }
    }

    private void logFileSize() {
        log.info("File size to process: {} MB", byte2MB(size));
    }

    private void logProgress() {
        long elapsedTime = System.currentTimeMillis() - start;
        long percent = (100 * processedBytes) / size;
        log.info("Processed {} MB in {} ms ({} %)", byte2MB(processedBytes), elapsedTime, percent);
    }

    private void logProcessingStats() {
        long elapsedTime = System.currentTimeMillis() - start;

        log.info("Index build in {} ms", elapsedTime);
        log.info("Lines: {}, size: {} MB", processedLines, size / HDD_MB);
    }

    private long byte2MB(long b) {
        return b / HDD_MB;
    }
}
