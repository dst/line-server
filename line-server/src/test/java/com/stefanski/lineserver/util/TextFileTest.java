package com.stefanski.lineserver.util;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 */
public class TextFileTest {

    private static TextFile textFile;

    @BeforeClass
    public static void setUp() throws IOException {
        URL foxUrl = TextFileTest.class.getResource("../fox.txt");
        textFile = new TextFile(foxUrl.getPath());
    }

    @Test
    public void shouldReturnTextForCorrectLineNr() throws TextFileException {
        Assert.assertEquals("the", textFile.getLine(1));
        Assert.assertEquals("quick brown", textFile.getLine(2));
        Assert.assertEquals("fox jumps over the", textFile.getLine(3));
        Assert.assertEquals("lazy dog", textFile.getLine(4));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNegativeLineNr() throws TextFileException {
        textFile.getLine(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForZeroLineNr() throws TextFileException {
        textFile.getLine(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForTooBigLineNr() throws TextFileException {
        textFile.getLine(5);
    }

    @Test
    public void shouldDetectValidLinesNr() {
        List<Integer> validLineNrs = Arrays.asList(1, 2, 3, 4);
        for (int lineNr : validLineNrs) {
            Assert.assertTrue(textFile.isLineNrValid(lineNr));
        }
    }

    @Test
    public void shouldDetectInvalidLinesNr() {
        List<Integer> invalidLineNrs = Arrays.asList(-5, -1, 0, 5, 10);
        for (int lineNr : invalidLineNrs) {
            Assert.assertFalse(textFile.isLineNrValid(lineNr));
        }
    }
}
