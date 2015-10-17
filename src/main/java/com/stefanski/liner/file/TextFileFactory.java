package com.stefanski.liner.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.stefanski.liner.index.IndexException;
import com.stefanski.liner.index.TextFileIndex;
import com.stefanski.liner.index.TextFileIndexer;
import com.stefanski.liner.index.TextFileIndexerFactory;
import com.stefanski.liner.util.SeekableByteChannelReader;

/**
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
@Component
public class TextFileFactory {

    /**
     * Creates TextFile from a file.
     * 
     * @param fileName
     * @return
     * @throws TextFileException
     */
    public TextFile createFromFile(String fileName) throws TextFileException {
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
