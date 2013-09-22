package com.stefanski.lineserver.index;

import static com.stefanski.lineserver.LineServerConstants.HDD_MB;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * Finds line offsets in a file channel (which represents a text file).
 * 
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
// It is not straightforward because of mapping usage.
class LineOffsetFinder {

    private static final int FILE_BUF_SIZE = 256 * HDD_MB;

    /**
     * A channel to a file.
     */
    private final FileChannel fileChannel;

    // TODO(dst), Sep 22, 2013: document it a little bit
    private final byte[] fileArr;
    private final long size;
    private long processed;
    private long offset;
    private long mappedBytes;
    private int arrPos;

    public LineOffsetFinder(FileChannel fileChannel) throws IOException {
        this.fileChannel = fileChannel;
        fileArr = new byte[FILE_BUF_SIZE];
        size = fileChannel.size();
        processed = 0;
        offset = 0;
        mappedBytes = 0;
        arrPos = -1;

        mapNextChunk();
    }

    public long getNextLineOffset() throws IOException {
        if (!hasNextLine()) {
            throw new IllegalStateException("There is no next line.");
        }

        long lineOffset = -1;
        while (lineOffset < 0) {
            lineOffset = findNextOffset();
            if (isArrProcessed()) {
                processed += mappedBytes;
                mapNextChunk();
            }
        }

        return lineOffset;
    }

    public boolean hasNextLine() {
        return processed < size;
    }

    private long findNextOffset() {
        while (!isArrProcessed()) {
            if (fileArr[arrPos] == '\n') {
                int lineLength = (int) (processed + arrPos - offset);
                assert lineLength >= 0;

                long prevOffset = offset;
                offset += lineLength + 1;
                arrPos++;
                return prevOffset;
            }
            arrPos++;
        }

        return -1;
    }

    private boolean isArrProcessed() {
        return arrPos < 0 || arrPos == mappedBytes;
    }

    private void mapNextChunk() throws IOException {
        long bytesToProcess = size - processed;
        long bytesToMap = Math.min(bytesToProcess, FILE_BUF_SIZE);
        assert bytesToMap <= Integer.MAX_VALUE;

        ByteBuffer mappedBuf = fileChannel.map(MapMode.READ_ONLY, processed, bytesToMap);
        mappedBuf.get(fileArr, 0, (int) bytesToMap);

        mappedBytes = bytesToMap;
        arrPos = 0;
    }
}
