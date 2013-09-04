package com.stefanski.lineserver.index;

/**
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class LineMetadata {
    public long begin;
    public int length;

    /**
     * @param begin
     * @param length
     */
    public LineMetadata(long begin, int length) {
        this.begin = begin;
        this.length = length;
    }

    @Override
    public String toString() {
        return "LineMetadata [begin=" + begin + " , length=" + length + "]";
    }
}
