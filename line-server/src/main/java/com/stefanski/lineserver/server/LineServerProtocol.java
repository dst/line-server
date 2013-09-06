package com.stefanski.lineserver.server;

import com.stefanski.lineserver.util.TextFile;

/**
 * A protocol of a line server.
 * 
 * It is thread-safe.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
// It is thread-safe because TextFile is thread-safe.
class LineServerProtocol {

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
    public String processGetCmd(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid format of get command: " + input);
        }

        long lineNr;
        try {
            lineNr = Long.valueOf(tokens[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse line number: " + tokens[1], e);
        }

        if (textFile.isLineNrValid(lineNr)) {
            return "OK\r\n" + textFile.getLine(lineNr);
        } else {
            return "ERR\r\n";
        }
    }
}
