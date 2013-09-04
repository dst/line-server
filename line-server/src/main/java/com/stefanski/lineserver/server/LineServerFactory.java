package com.stefanski.lineserver.server;

import java.io.IOException;

import com.stefanski.lineserver.util.TextFile;

/**
 * A factory for creating line servers.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class LineServerFactory {

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
            TextFile textFile = new TextFile(fileName);
            return new LineServer(textFile);
        } catch (IOException e) {
            throw new LineServerException("Cannot create TextFile", e);
        }
    }
}
