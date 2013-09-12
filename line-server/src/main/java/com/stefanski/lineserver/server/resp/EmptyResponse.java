package com.stefanski.lineserver.server.resp;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class EmptyResponse implements Response {
    // TODO(dst), Sep 12, 2013: it can be singleton

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(PrintWriter writer) throws IOException {
        // Do nothing
    }

}
