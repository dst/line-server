package com.stefanski.liner.index;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A metadata of line.
 * 
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
@ToString
@Getter
@RequiredArgsConstructor
public class LineMetadata {
    /**
     * An offset from beginning of the file.
     */
    private final long offset;

    /**
     * Line's length without EOL.
     */
    private final int length;
}
