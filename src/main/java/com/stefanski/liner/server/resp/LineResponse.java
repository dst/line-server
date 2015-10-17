package com.stefanski.liner.server.resp;

import java.io.IOException;
import java.io.PrintWriter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A response for a line command.
 * 
 * @author Dariusz Stefanski
 * @date Sep 7, 2013
 */
@EqualsAndHashCode
@ToString
public class LineResponse implements Response {

    private static final String RESP_END_OF_LINE = "\n";
    private static final String OK = "OK" + RESP_END_OF_LINE;
    private static final String ERROR = "ERROR" + RESP_END_OF_LINE;

    private final String status;
    private final String line;

    public static LineResponse createOkResp(String line) {
        return new LineResponse(OK, line);
    }

    public static LineResponse createErrResp() {
        return new LineResponse(ERROR, null);
    }

    public LineResponse(String status, String line) {
        this.status = status;
        this.line = line;
    }

    @Override
    public void write(PrintWriter writer) throws IOException {
        writer.print(status);
        if (line != null) {
            writer.println(line);
        }
    }
}
