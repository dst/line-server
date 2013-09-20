package com.stefanski.lineserver.server;

import com.stefanski.lineserver.file.IndexedTextFile;
import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.file.TextFileException;

/**
 * A factory for creating line servers.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class LineServerFactory {

    private LineServerFactory() {
    }

    /**
     * Creates new line server associated with specified file.
     * 
     * @param fileName
     *            The name of an immutable text file
     * @return A new line server
     * @throws LineServerException
     *             If server cannot be created
     */
    public static LineServer createServer(String fileName) throws LineServerException {
        try {
            TextFile textFile = IndexedTextFile.createFromFile(fileName);
            return new LineServer(textFile);
        } catch (TextFileException e) {
            throw new LineServerException("Cannot create TextFile", e);
        }
    }
}
