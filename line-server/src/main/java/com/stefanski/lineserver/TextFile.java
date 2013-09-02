package com.stefanski.lineserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a text file from file system.
 * 
 * The text file must have the following properties:
 * <ul>
 * <li>Each line is terminated with a newline ("\n")</li>
 * <li>Any given line will fit into memory</li>
 * <li>The line is valid ASCII (e.g. not Unicode)</li>
 * <ul>
 * 
 * A file is pre-processed at the beginning for a future good performance.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
class TextFile {

    /**
     * A path of the text file.
     */
    private final Path path;

    /**
     * FileChannel for a random access to file. It is used when getting specified line number.
     */
    private final FileChannel fileChannel;

    /**
     * line number -> line metadata
     */
    // Assumption: it will fit in RAM
    private final List<LineMetadata> lineMetadatas;

    /**
     * Creates object from a file.
     * 
     * @param fileName
     * @throws IOException
     *             If an I/O error occurs during processing file
     */
    public TextFile(String fileName) throws IOException {
        path = FileSystems.getDefault().getPath(fileName);
        fileChannel = FileChannel.open(path, StandardOpenOption.READ);
        lineMetadatas = new ArrayList<LineMetadata>();
        process();
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
    public String getLine(int lineNr) {
        if (lineNr < 1 || lineNr > getLinesCount()) {
            throw new IllegalArgumentException("Invalid line number: " + lineNr);
        }

        // The first line of the file is line 1
        LineMetadata lineMetadata = lineMetadatas.get(lineNr - 1);

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
            StdLogger.error("Error when reading line: " + e);
            return "";
        }
    }

    private void process() throws IOException {
        StdLogger.info("Processing file");

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {
            String line;
            long position = 0;
            while ((line = reader.readLine()) != null) {
                LineMetadata metadata = new LineMetadata(position, line.length());
                lineMetadatas.add(metadata);
                position += line.length() + 1; // 1 for \n
            }
        }

        StdLogger.info("File processed");
    }

    private int getLinesCount() {
        return lineMetadatas.size();
    }

    static class LineMetadata {
        long begin;
        int length;

        /**
         * @param begin
         * @param length
         */
        public LineMetadata(long begin, int length) {
            this.begin = begin;
            this.length = length;
        }

        @Override
        public String toString() {
            return "LineMetadata [begin=" + begin + ", length=" + length + "]";
        }
    }
}
