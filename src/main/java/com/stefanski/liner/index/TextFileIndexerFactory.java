package com.stefanski.liner.index;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
@Slf4j
public class TextFileIndexerFactory {

    private static final String INDEX_SUFFIX = "_index";

    private TextFileIndexerFactory() {
    }

    public static TextFileIndexer createIndexer(Path filePath) {
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
        Path indexPath = getIndexPath(filePath);
        deleteIfExists(indexPath);
        return Files.createFile(indexPath);
    }

    private static Path getIndexPath(Path filePath) {
        String indexFileName = filePath.toString() + INDEX_SUFFIX;
        return FileSystems.getDefault().getPath(indexFileName);
    }

    private static void deleteIfExists(Path indexPath) throws IOException {
        if (Files.exists(indexPath)) {
            log.info("Index file {} exists. Removing", indexPath);
            Files.delete(indexPath);
        }
    }
}
