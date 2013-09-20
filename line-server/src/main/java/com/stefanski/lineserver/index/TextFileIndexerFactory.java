package com.stefanski.lineserver.index;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.stefanski.lineserver.util.StdLogger;

/**
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
public class TextFileIndexerFactory {

    private TextFileIndexerFactory() {
    }

    public static TextFileIndexer createIndexer(Path filePath) throws IndexException {
        try {
            FileChannel fileFC = FileChannel.open(filePath, READ);

            Path indexPath = createIndexFile(filePath);
            FileChannel indexFC = FileChannel.open(indexPath, READ, WRITE);

            return new TextFileIndexer(fileFC, indexFC);
        } catch (IOException e) {
            throw new IndexException("Cannot create indexer", e);
        }
    }

    private static Path createIndexFile(Path filePath) throws IOException {
        String indexFileName = filePath.toString() + "_lineServerIndex";
        Path indexPath = FileSystems.getDefault().getPath(indexFileName);

        // Delete if exists
        if (Files.exists(indexPath)) {
            StdLogger.info(String.format("Index file ('%s') exists. Removing", indexPath));
            Files.delete(indexPath);
        }

        // and create
        indexPath = Files.createFile(indexPath);

        return indexPath;
    }
}
