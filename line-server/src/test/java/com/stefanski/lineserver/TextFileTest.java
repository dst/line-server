package com.stefanski.lineserver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 */
public class TextFileTest {

    private TextFile textFile;

    @Before
    public void setUp() {
        textFile = new TextFile("fox.txt");
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
