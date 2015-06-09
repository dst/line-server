package com.stefanski.liner.index;

import static java.nio.file.StandardOpenOption.READ;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
public class LineOfffsetFinderTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    private LineOffsetFinder lineFinder;

    @Before
    public void setUp() throws IOException {
        File file = folder.newFile("file.txt");
        FileWriter out = new FileWriter(file);
        out.write("line1\n");
        out.write("line2\n");
        out.write("line3\n");
        out.close();

        Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
        FileChannel channel = FileChannel.open(path, READ);

        lineFinder = new LineOffsetFinder(channel);
    }

    @Test
    public void shouldReturnCorrectLineOffsets() throws IOException {
        assertEquals(0, lineFinder.getNextLineOffset());
        assertEquals(6, lineFinder.getNextLineOffset());
        assertEquals(12, lineFinder.getNextLineOffset());
    }

    @Test
    public void shouldHasNextLineWorks() throws IOException {
        assertTrue(lineFinder.hasNextLine());
        lineFinder.getNextLineOffset();
        lineFinder.getNextLineOffset();
        lineFinder.getNextLineOffset();
        assertFalse(lineFinder.hasNextLine());
    }
}
