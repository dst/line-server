package com.stefanski.lineserver.index;

/**
 * The index of a file.
 * 
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
public interface TextFileIndex extends AutoCloseable {

    /**
     * Gets a metadata of a specified line from the index.
     * 
     * @param lineNr
     * @return
     * @throws IndexException
     */
    LineMetadata getLineMetadata(long lineNr) throws IndexException;

    /**
     * @return A line count in a file which was indexed
     */
    long getLineCount();
}
