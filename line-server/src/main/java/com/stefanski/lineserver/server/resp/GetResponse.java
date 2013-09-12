package com.stefanski.lineserver.server.resp;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * A response to client
 * 
 * @author Dariusz Stefanski
 * @date Sep 7, 2013
 */
public class GetResponse implements Response {

    static final String RESP_END_OF_LINE = "\r\n";
    private static final String OK = "OK" + RESP_END_OF_LINE;
    private static final String ERROR = "ERR" + RESP_END_OF_LINE;

    String status;
    String line;

    public static GetResponse createOkResp(String line) {
        return new GetResponse(OK, line);
    }

    public static GetResponse createErrResp() {
        return new GetResponse(ERROR, null);
    }

    /**
     * @param status
     * @param line
     */
    public GetResponse(String status, String line) {
        this.status = status;
        this.line = line;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(PrintWriter writer) throws IOException {
        writer.print(status);
        if (line != null) {
            writer.println(line);
        }
    }

    @Override
    public String toString() {
        return "GetResponse [status=" + status + ", line=" + line + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((line == null) ? 0 : line.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GetResponse other = (GetResponse) obj;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }
}