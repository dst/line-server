package com.stefanski.lineserver.index;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.File;
import java.io.FileWriter;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class TextFileIndexerTest {

    @Rule
    public final static TemporaryFolder folder = new TemporaryFolder();

    private static TextFileIndex index;

    @BeforeClass
    public static void setUp() throws Exception {
        File file = folder.newFile("file.txt");
        FileWriter out = new FileWriter(file);
        out.write("line1\n");
        out.write("line2\n");
        out.close();

        Path filePath = FileSystems.getDefault().getPath(file.getAbsolutePath());
        FileChannel fileFC = FileChannel.open(filePath, READ);

        File indexFile = folder.newFile("file.txt_lineServerIndex");
        Path indexPath = FileSystems.getDefault().getPath(indexFile.getAbsolutePath());
        FileChannel indexFC = FileChannel.open(indexPath, READ, WRITE);

        TextFileIndexer indexer = new TextFileIndexer(fileFC, indexFC);
        index = indexer.buildIndex();
    }

    @Test
    public void shouldLineCountBeCorrect() {
        Assert.assertEquals(2, index.getLineCount());
    }

    @Test
    public void shouldLineMetadataBeCorrectForLine1() throws IndexException {
        // line1
        LineMetadata meta = index.getLineMetadata(1);
        Assert.assertEquals(0, meta.offset);
        Assert.assertEquals(5, meta.length);
    }

    @Test
    public void shouldLineMetadataBeCorrectForLine2() throws IndexException {
        // line2
        LineMetadata meta = index.getLineMetadata(2);
        Assert.assertEquals(6, meta.offset);
        Assert.assertEquals(5, meta.length);
    }
}
