package com.stefanski.lineserver.server;

/**
 * A response to client
 * 
 * @author Dariusz Stefanski
 * @date Sep 7, 2013
 */
public class Response {

    static final String RESP_END_OF_LINE = "\r\n";
    private static final String OK = "OK" + RESP_END_OF_LINE;
    private static final String ERROR = "ERR" + RESP_END_OF_LINE;

    String status;
    String line;

    static Response createOkResp(String line) {
        return new Response(OK, line);
    }

    static Response createErrResp() {
        return new Response(ERROR, null);
    }

    /**
     * @param status
     * @param line
     */
    public Response(String status, String line) {
        this.status = status;
        this.line = line;
    }

    @Override
    public String toString() {
        return line != null ? status + line : status;
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
        Response other = (Response) obj;
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