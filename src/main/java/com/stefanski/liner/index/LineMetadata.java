package com.stefanski.liner.index;

import lombok.Getter;
import lombok.ToString;

/**
 * A metadata of line.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
@ToString
@Getter
public class LineMetadata {
    /**
     * An offset from beginning of the file.
     */
    private final long offset;

    /**
     * Line's length without EOL.
     */
    private final int length;

    /**
     * @param begin
     * @param length
     */
    public LineMetadata(long begin, int length) {
        this.offset = begin;
        this.length = length;
    }
}
