package com.stefanski.lineserver.index;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stefanski.lineserver.util.TextFileTest;

/**
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class TextFileIndexerTest {

    private static TextFileIndex index;

    @BeforeClass
    public static void setUp() throws IOException {
        URL foxUrl = TextFileTest.class.getResource("../fox.txt");
        Path foxPath = FileSystems.getDefault().getPath(foxUrl.getPath());
        index = TextFileIndexer.buildIndex(foxPath);
    }

    @Test
    public void shouldLineCountBeCorrect() {
        Assert.assertEquals(4, index.getLineCount());
    }

    @Test
    public void shouldLineMetadataBeCorrectForLine1() throws IndexException {
        // the
        LineMetadata meta1 = index.getLineMetadata(1);
        Assert.assertEquals(0, meta1.begin);
        Assert.assertEquals(3, meta1.length);
    }

    @Test
    public void shouldLineMetadataBeCorrectForLine2() throws IndexException {
        // the
        LineMetadata meta1 = index.getLineMetadata(2);
        Assert.assertEquals(4, meta1.begin);
        Assert.assertEquals(11, meta1.length);
    }
}
