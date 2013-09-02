package com.stefanski.lineserver;

import java.io.IOException;
import java.net.URL;

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
        URL foxUrl = TextFileTest.class.getResource("fox.txt");
        textFile = new TextFile(foxUrl.getPath());
    }

    @Test
    public void shouldReturnTextForCorrectLineNr() {
        Assert.assertEquals("the", textFile.getLine(1));
        Assert.assertEquals("quick brown", textFile.getLine(2));
        Assert.assertEquals("fox jumps over the", textFile.getLine(3));
        Assert.assertEquals("lazy dog", textFile.getLine(4));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNegativeLineNr() {
        textFile.getLine(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForZeroLineNr() {
        textFile.getLine(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForTooBigLineNr() {
        textFile.getLine(5);
    }
}
