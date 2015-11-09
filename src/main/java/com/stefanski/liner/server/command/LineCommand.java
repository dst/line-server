package com.stefanski.liner.server.command;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.response.LineResponse;
import com.stefanski.liner.server.response.Response;

/**
 * A request to send specified line from a file.
 *
 * @author Dariusz Stefanski
 * @since Sep 12, 2013
 */
@Slf4j
@EqualsAndHashCode
public class LineCommand implements Command {

    private final long lineNr;

    public LineCommand(long lineNr) {
        this.lineNr = lineNr;
    }

    @Override
    public Response execute(CommandContext ctx) {
        long start = 0;

        if (log.isTraceEnabled()) {
            start = System.currentTimeMillis();
        }

        LineResponse resp = createResponse(ctx);

        if (log.isTraceEnabled()) {
            long elapsedTime = System.currentTimeMillis() - start;
            log.trace("Get request responded in {}", elapsedTime);
        }

        return resp;
    }

    private LineResponse createResponse(CommandContext ctx) {
        TextFile textFile = ctx.getTextFile();
        try {
            String line = textFile.getLine(lineNr);
            return LineResponse.ok(line);
        } catch (TextFileException e) {
            log.error("Cannot get line: ", e);
            return LineResponse.error();
        }
    }
}
