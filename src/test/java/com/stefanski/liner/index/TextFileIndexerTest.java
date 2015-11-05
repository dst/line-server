package com.stefanski.liner.index;

import java.io.File;
import java.io.FileWriter;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * @author Dariusz Stefanski
 * @since Sep 4, 2013
 */
public class TextFileIndexerTest {

    @ClassRule
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

        File indexFile = folder.newFile("file.txt_index");
        Path indexPath = FileSystems.getDefault().getPath(indexFile.getAbsolutePath());
        FileChannel indexFC = FileChannel.open(indexPath, READ, WRITE);

        TextFileIndexer indexer = new TextFileIndexer(fileFC, indexFC);
        index = indexer.buildIndex();
        indexer.close();
    }

    @Test
    public void shouldLineCountBeCorrect() {
        Assert.assertEquals(2, index.getLineCount());
    }

    @Test
    public void shouldLineMetadataBeCorrectForLine1() {
        // line1
        LineMetadata meta = index.getLineMetadata(1);
        Assert.assertEquals(0, meta.getOffset());
        Assert.assertEquals(5, meta.getLength());
    }

    @Test
    public void shouldLineMetadataBeCorrectForLine2() {
        // line2
        LineMetadata meta = index.getLineMetadata(2);
        Assert.assertEquals(6, meta.getOffset());
        Assert.assertEquals(5, meta.getLength());
    }
}
