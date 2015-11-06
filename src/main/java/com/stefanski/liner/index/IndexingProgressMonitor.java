package com.stefanski.liner.index;

import java.time.Instant;

import lombok.extern.slf4j.Slf4j;

import static com.stefanski.liner.LinerConstants.HDD_MB;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * Reports progress of indexing a file.
 *
 * @author Dariusz Stefanski
 * @since Sep 19, 2013
 */
@Slf4j
class IndexingProgressMonitor {
    private static final long CHUNK_SIZE = 100 * HDD_MB;

    private final long size;
    private Instant start;
    private long processedLines = 0;
    private long processedBytes = 0;
    private int chunkNr = 1;

    /**
     *
     * @param size
     *            A size of a processed file
     */
    public IndexingProgressMonitor(long size) {
        this.size = size;
        logFileSize();
    }

    public void start() {
        start = now();
    }

    public void stop() {
        logProcessingStats();
    }

    /**
     * This method should be called after processing each line.
     *
     * @param startPositionOfLine
     *            A starting position of a processed line in file
     */
    public void processedLine(long startPositionOfLine) {
        processedLines++;
        processedBytes = startPositionOfLine;

        if (processedBytes > CHUNK_SIZE * chunkNr) {
            logProgress();
            chunkNr++;
        }
    }

    private void logFileSize() {
        log.info("File size to process: {} MB", byte2MB(size));
    }

    private void logProgress() {
        log.info("Processed {} MB in {} ({} %)",
                processedMegaBytes(), elapsedTime(), processingPercent());
    }

    private void logProcessingStats() {
        log.info("Index build in {}", elapsedTime());
        log.info("Lines: {}, size: {} MB", processedLines, processedMegaBytes());
    }

    private long processingPercent() {
        return (100 * processedBytes) / size;
    }

    private String elapsedTime() {
        long msTotal = MILLIS.between(start, now());
        long s = msTotal / 1000;
        long ms = msTotal % 1000;
        return String.format("%ss %sms", s, ms);
    }

    private long processedMegaBytes() {
        return byte2MB(processedBytes);
    }

    private long byte2MB(long b) {
        return b / HDD_MB;
    }
}
