package com.stefanski.liner.server.cmd;

import lombok.extern.slf4j.Slf4j;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.resp.LineResponse;
import com.stefanski.liner.server.resp.Response;

/**
 * A request to send specified line from a file.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
@Slf4j
public class LineCommand implements Command {

    private final long lineNr;

    public LineCommand(long lineNr) {
        this.lineNr = lineNr;
    }

    /**
     * {@inheritDoc}
     */
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

        if (!textFile.isLineNrValid(lineNr)) {
            log.error("Invalid line nr: {}", lineNr);
            return LineResponse.createErrResp();
        }

        try {
            String line = textFile.getLine(lineNr);
            return LineResponse.createOkResp(line);
        } catch (TextFileException e) {
            log.error("Cannot get line: ", e);
            return LineResponse.createErrResp();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (lineNr ^ (lineNr >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LineCommand other = (LineCommand) obj;
        if (lineNr != other.lineNr) {
            return false;
        }
        return true;
    }
}
