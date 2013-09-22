package com.stefanski.lineserver.server.resp;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * A response from a server to a client.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public interface Response {

    /**
     * Writes a response using writer.
     * 
     * @param writer
     * @throws IOException
     */
    void write(PrintWriter writer) throws IOException;

}
