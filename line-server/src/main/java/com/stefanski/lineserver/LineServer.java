package com.stefanski.lineserver;

import java.io.IOException;

/**
 * Network server that serves individual lines of an immutable text file over the network to clients
 * using the following protocol:
 * 
 * - GET nnnn
 * 
 * If nnnn is a valid line number for the given text file, return "OK\r\n" and then the nnnn-th line
 * of the specified text file.
 * 
 * If nnnn is not a valid line number for the given text file, return "ERR\r\n".
 * 
 * The first line of the file is line 1 (not line 0).
 * 
 * - QUIT
 * 
 * Disconnect client
 * 
 * - SHUTDOWN
 * 
 * Shutdown the server
 * 
 * The server listens for connections TCP port 10497.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
class LineServer {

    private final TextFile textFile;

    /**
     * Factory for easy creating servers.
     * 
     * @param fileName
     *            The name of an immutable text file
     * @return A new line server
     * @throws LineServerException
     *             If server cannot be created
     */
    public static LineServer create(String fileName) throws LineServerException {
        try {
            TextFile textFile = new TextFile(fileName);
            return new LineServer(textFile);
        } catch (IOException e) {
            throw new LineServerException("Cannot create TextFile", e);
        }
    }

    /**
     * @param textFile
     *            An immutable text file
     */
    public LineServer(TextFile textFile) {
        this.textFile = textFile;
    }

    /**
     * Runs server.
     */
    public void run() {
        // TODO(dst), Sep 1, 2013: impl
        StdLogger.info("Starting server...");
        StdLogger.info(textFile.getLine(1));
        StdLogger.info("Server stopped.");
    }

}
