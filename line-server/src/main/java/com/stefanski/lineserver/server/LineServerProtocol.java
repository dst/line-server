package com.stefanski.lineserver.server;

import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.file.TextFileException;
import com.stefanski.lineserver.server.cmd.GetCommand;
import com.stefanski.lineserver.server.resp.GetResponse;
import com.stefanski.lineserver.util.StdLogger;

/**
 * A protocol of a line server.
 * 
 * It is thread-safe.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
// It is thread-safe because TextFile is thread-safe.
public class LineServerProtocol {
    // TODO(dst), Sep 12, 2013: move to GetCommand?

    /**
     * An immutable text file to serve over the network.
     */
    private final TextFile textFile;

    public LineServerProtocol(TextFile textFile) {
        this.textFile = textFile;
    }

    /**
     * GET nnnn
     * 
     * If nnnn is a valid line number for the given text file, return "OK\r\n" and then the nnnn-th
     * line of the specified text file.
     * 
     * If nnnn is not a valid line number for the given text file, return "ERR\r\n".
     * 
     * The first line of the file is line 1 (not line 0).
     * 
     * @param input
     * @return Response to client
     */
    public GetResponse processGetCmd(GetCommand cmd) {
        if (!textFile.isLineNrValid(cmd.getLineNr())) {
            StdLogger.error("Invalid line nr: " + cmd.getLineNr());
            return GetResponse.createErrResp();
        }

        try {
            String line = textFile.getLine(cmd.getLineNr());
            return GetResponse.createOkResp(line);
        } catch (TextFileException e) {
            StdLogger.error("Cannot get line: " + e);
            return GetResponse.createErrResp();
        }
    }
}
