package com.stefanski.lineserver.index;

/**
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class LineMetadata {
    public long offset;
    public int length;

    /**
     * @param begin
     * @param length
     */
    public LineMetadata(long begin, int length) {
        this.offset = begin;
        this.length = length;
    }

    @Override
    public String toString() {
        return "LineMetadata [offset=" + offset + " , length=" + length + "]";
    }
}
