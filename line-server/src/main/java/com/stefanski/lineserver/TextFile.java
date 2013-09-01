package com.stefanski.lineserver;

/**
 * Represents a text file from file system.
 * 
 * The text file must have the following properties:
 * <ul>
 * <li>Each line is terminated with a newline ("\n")</li>
 * <li>Any given line will fit into memory</li>
 * <li>The line is valid ASCII (e.g. not Unicode)</li>
 * <ul>
 * 
 * A file is pre-processed at the beginning for a future good performance.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
class TextFile {

    /**
     * Creates object from a file.
     * 
     * @param fileName
     */
    public TextFile(String fileName) {
        // TODO(dst), Sep 1, 2013: init
    }

    /**
     * Returns specified line from a file.
     * 
     * @param lineNr
     * @return line from a file
     */
    public String getLine(long lineNr) {
        // TODO(dst), Sep 1, 2013: impl
        return "Line from file";
    }

}
