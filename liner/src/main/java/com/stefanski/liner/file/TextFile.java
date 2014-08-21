package com.stefanski.liner.file;


/**
 * Represents a text file from a file system.
 * 
 * The text file must have the following properties:
 * <ul>
 * <li>Each line is terminated with a newline ("\n")</li>
 * <li>Any given line will fit into memory</li>
 * <li>The line is valid ASCII (e.g. not Unicode)</li>
 * </ul>
 * 
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
public interface TextFile extends AutoCloseable {

    /**
     * @param lineNr
     * @return true if there is such line number in the file
     */
    boolean isLineNrValid(long lineNr);

    /**
     * Returns specified line from a file.
     * 
     * The first line of the file is line 1 (not line 0).
     * 
     * @param lineNr
     *            Number of line to read
     * @return A line from a file
     * @throws TextFileException
     *             If the line cannot be read
     * @throws IllegalArgumentException
     *             If lineNr is invalid
     */
    String getLine(long lineNr) throws TextFileException;
}
