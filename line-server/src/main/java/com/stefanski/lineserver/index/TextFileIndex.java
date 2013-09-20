package com.stefanski.lineserver.index;

/**
 * The index of file.
 * 
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
public interface TextFileIndex {

    /**
     * Gets a metadata of specified line from the index.
     * 
     * @param lineNr
     * @return
     * @throws IndexException
     */
    LineMetadata getLineMetadata(long lineNr) throws IndexException;

    /**
     * @return Line count in a file which was indexed
     */
    long getLineCount();
}
