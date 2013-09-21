package com.stefanski.lineserver.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.stefanski.lineserver.index.IndexException;
import com.stefanski.lineserver.index.TextFileIndex;
import com.stefanski.lineserver.index.TextFileIndexer;
import com.stefanski.lineserver.index.TextFileIndexerFactory;
import com.stefanski.lineserver.util.SeekableByteChannelReader;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class TextFileFactory {

    /**
     * Creates TextFile from a file.
     * 
     * @param fileName
     * @return
     * @throws TextFileException
     */
    public static TextFile createFromFile(String fileName) throws TextFileException {
        try {
            Path path = FileSystems.getDefault().getPath(fileName);
            SeekableByteChannelReader fileReader = SeekableByteChannelReader.fromFilePath(path);

            TextFileIndexer indexer = TextFileIndexerFactory.createIndexer(path);
            TextFileIndex index = indexer.buildIndex();
            indexer.close();

            return new IndexedTextFile(fileReader, index);
        } catch (IOException | IndexException e) {
            throw new TextFileException("Error during creating TextFile", e);
        }
    }
}
