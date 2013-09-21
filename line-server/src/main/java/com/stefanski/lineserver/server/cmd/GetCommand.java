package com.stefanski.lineserver.server.cmd;

import com.stefanski.lineserver.server.ClientHandler;
import com.stefanski.lineserver.server.resp.GetResponse;
import com.stefanski.lineserver.server.resp.Response;
import com.stefanski.lineserver.util.StdLogger;

/**
 * A request to send specified line from a file.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class GetCommand implements Command {

    private final long lineNr;

    public GetCommand(long lineNr) {
        this.lineNr = lineNr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(ClientHandler handler) {
        long start = 0;

        if (StdLogger.isTraceEnabled()) {
            start = System.currentTimeMillis();
        }

        // // TODO(dst), Sep 12, 2013: protocol in this class?
        GetResponse resp = handler.getProtocol().processGetCmd(this);

        if (StdLogger.isTraceEnabled()) {
            long elapsedTime = System.currentTimeMillis() - start;
            StdLogger.trace("Get request responded in " + elapsedTime);
        }

        return resp;
    }

    public long getLineNr() {
        return lineNr;
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
        GetCommand other = (GetCommand) obj;
        if (lineNr != other.lineNr) {
            return false;
        }
        return true;
    }
}
