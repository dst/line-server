package com.stefanski.lineserver.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.stefanski.lineserver.index.IndexException;
import com.stefanski.lineserver.index.LineMetadata;
import com.stefanski.lineserver.index.TextFileIndex;
import com.stefanski.lineserver.index.TextFileIndexer;

/**
 * Represents a text file from file system.
 * 
 * The text file must have the following properties:
 * <ul>
 * <li>Each line is terminated with a newline ("\n")</li>
 * <li>Any given line will fit into memory</li>
 * <li>The line is valid ASCII (e.g. not Unicode)</li>
 * </ul>
 * 
 * A file is pre-processed at the beginning for a future good performance.
 * 
 * It is thread-safe.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
public class TextFile {

    /**
     * FileChannel for a random access to file. It is used when getting specified line number.
     */
    private final FileChannel fileChannel;

    /**
     * File index for faster line lookups.
     */
    private final TextFileIndex index;

    /**
     * Creates object from a file.
     * 
     * @param fileName
     * @throws IOException
     *             If an I/O error occurs during processing file
     */
    public TextFile(String fileName) throws IOException {
        Path path = FileSystems.getDefault().getPath(fileName);
        fileChannel = FileChannel.open(path, StandardOpenOption.READ);

        TextFileIndexer indexer = TextFileIndexer.createIndexer(path);
        index = indexer.buildIndex();
    }

    public boolean isLineNrValid(long lineNr) {
        return lineNr >= 1 && lineNr <= getLineCount();
    }

    /**
     * Returns specified line from a file.
     * 
     * The first line of the file is line 1 (not line 0).
     * 
     * @param lineNr
     *            Number of line to read
     * @return A line from a file
     */
    public synchronized String getLine(long lineNr) {
        if (lineNr < 1 || lineNr > getLineCount()) {
            throw new IllegalArgumentException("Invalid line number: " + lineNr);
        }

        LineMetadata lineMetadata;
        try {
            lineMetadata = index.getLineMetadata(lineNr);
        } catch (IndexException e) {
            // TODO(dst), Sep 4, 2013: throw exception
            StdLogger.error("Error when getting line metadata: " + e);
            return "";
        }

        try {
            // Set starting position in file
            fileChannel.position(lineMetadata.begin);

            // and read until a whole line will be in buffer
            ByteBuffer line = ByteBuffer.allocate(lineMetadata.length);
            int readCount;
            do {
                readCount = fileChannel.read(line);
            } while (readCount != -1 && line.hasRemaining());

            // TODO(dst), Sep 2, 2013: String is not effective
            return new String(line.array(), "ASCII");
        } catch (IOException e) {
            // TODO(dst), Sep 4, 2013: throw exception
            StdLogger.error("Error when reading line: " + e);
            return "";
        }
    }

    private long getLineCount() {
        return index.getLineCount();
    }
}
