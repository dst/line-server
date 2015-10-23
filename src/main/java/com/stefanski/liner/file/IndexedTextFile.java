package com.stefanski.liner.file;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.stefanski.liner.index.IndexException;
import com.stefanski.liner.index.LineMetadata;
import com.stefanski.liner.index.TextFileIndex;
import com.stefanski.liner.util.SeekableByteChannelReader;

/**
 * A text file with an index to speed up reading specified lines.
 *
 * The file is pre-processed at the beginning for a future good performance.
 * 
 * It is thread-safe.
 *
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 *
 */
public class IndexedTextFile implements TextFile {

    /**
     * A reader for a random access to file. It is used when getting specified line number.
     */
    private final SeekableByteChannelReader fileReader;

    /**
     * A file index for faster line lookups.
     */
    private final TextFileIndex index;

    public IndexedTextFile(SeekableByteChannelReader fileReader, TextFileIndex index) {
        this.fileReader = fileReader;
        this.index = index;
    }

    public boolean isLineNrValid(long lineNr) {
        return lineNr >= 1 && lineNr <= getLineCount();
    }

    public synchronized String getLine(long lineNr) {
        if (lineNr < 1 || lineNr > getLineCount()) {
            throw new IllegalArgumentException("Invalid line number: " + lineNr);
        }

        LineMetadata lineMetadata;
        try {
            lineMetadata = index.getLineMetadata(lineNr);
        } catch (IndexException e) {
            throw new TextFileException("Error when getting line metadata nr " + lineNr, e);
        }

        try {
            ByteBuffer line = fileReader.read(lineMetadata.getOffset(), lineMetadata.getLength());

            // TODO(dst), Sep 2, 2013: creating String is not a good idea here (memory + coping). It
            // would be better to use byte[]
            return new String(line.array(), "ASCII");
        } catch (IOException e) {
            throw new TextFileException("Error when reading line: " + lineNr, e);
        }
    }

    private long getLineCount() {
        return index.getLineCount();
    }

    @Override
    public void close() throws Exception {
        fileReader.close();
        index.close();
    }
}
