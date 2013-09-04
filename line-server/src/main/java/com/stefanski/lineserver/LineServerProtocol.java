package com.stefanski.lineserver;

/**
 * A protocol of a line server.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
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
     * @throws LineServerException
     *             If command cannot be handled
     */
    public String processGetCmd(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid format of get command: " + input);
        }

        int lineNr;
        try {
            lineNr = Integer.valueOf(tokens[1]);
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
