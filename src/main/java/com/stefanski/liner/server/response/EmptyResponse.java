package com.stefanski.liner.server.response;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * An empty response.
 *
 * @author Dariusz Stefanski
 * @since Sep 12, 2013
 */
public class EmptyResponse implements Response {

    private static final EmptyResponse INSTANCE = new EmptyResponse();

    public static EmptyResponse getInstance() {
        return INSTANCE;
    }

    private EmptyResponse() {
    }

    @Override
    public void write(PrintWriter writer) throws IOException {
        // Do nothing
    }
}
