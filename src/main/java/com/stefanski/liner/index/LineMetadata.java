package com.stefanski.liner.index;

import lombok.ToString;

/**
 * A metadata of line.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
@ToString
public class LineMetadata {
    /**
     * An offset from beginning of the file.
     */
    public long offset;

    /**
     * Line's length without EOL.
     */
    public int length;

    /**
     * @param begin
     * @param length
     */
    public LineMetadata(long begin, int length) {
        this.offset = begin;
        this.length = length;
    }
}
