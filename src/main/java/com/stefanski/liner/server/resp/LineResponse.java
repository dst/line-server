package com.stefanski.liner.server.resp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * A response for a line command.
 *
 * @author Dariusz Stefanski
 * @since Sep 7, 2013
 */
@EqualsAndHashCode
@ToString
public class LineResponse implements Response {

    private static final String RESP_END_OF_LINE = "\n";
    private static final String OK = "OK" + RESP_END_OF_LINE;
    private static final String ERROR = "ERROR" + RESP_END_OF_LINE;

    private final String status;
    private final Optional<String> line;

    public static LineResponse createOkResp(String line) {
        return new LineResponse(OK, of(line));
    }

    public static LineResponse createErrResp() {
        return new LineResponse(ERROR, empty());
    }

    private LineResponse(String status, Optional<String> line) {
        this.status = status;
        this.line = line;
    }

    @Override
    public void write(PrintWriter writer) throws IOException {
        writer.print(status);
        if (line.isPresent()) {
            writer.println(line.get());
        }
    }
}
