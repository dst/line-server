package com.stefanski.liner.server.resp;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * An empty response.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class EmptyResponse implements Response {

    private static EmptyResponse INSTANCE = new EmptyResponse();

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
