package com.stefanski.lineserver.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.stefanski.lineserver.index.IndexException;
import com.stefanski.lineserver.index.LineMetadata;
import com.stefanski.lineserver.index.TextFileIndex;
import com.stefanski.lineserver.index.TextFileIndexer;
import com.stefanski.lineserver.index.TextFileIndexerFactory;
import com.stefanski.lineserver.util.SeekableByteChannelReader;

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

    /**
     * Creates IndexedTextFile from a file.
     * 
     * @param fileName
     * @return
     * @throws IOException
     *             If an I/O error occurs during processing file
     * @throws IndexException
     */
    public static TextFile createFromFile(String fileName) throws TextFileException {
        try {
            Path path = FileSystems.getDefault().getPath(fileName);
            SeekableByteChannelReader fileReader = SeekableByteChannelReader.fromFilePath(path);

            TextFileIndexer indexer = TextFileIndexerFactory.createIndexer(path);
            TextFileIndex index = indexer.buildIndex();

            return new IndexedTextFile(fileReader, index);
        } catch (IOException | IndexException e) {
            throw new TextFileException("Error during creating TextFile", e);
        }
    }

    public IndexedTextFile(SeekableByteChannelReader fileReader, TextFileIndex index) {
        this.fileReader = fileReader;
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLineNrValid(long lineNr) {
        return lineNr >= 1 && lineNr <= getLineCount();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized String getLine(long lineNr) throws TextFileException {
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
            ByteBuffer line = fileReader.read(lineMetadata.offset, lineMetadata.length);

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
}
