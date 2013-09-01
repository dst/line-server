package com.stefanski.lineserver;

/**
 * Network server that serves individual lines of an immutable text file over the network to clients using the following
 * protocol:
 * 
 * - GET nnnn
 * 
 * If nnnn is a valid line number for the given text file, return "OK\r\n" and then the nnnn-th line of the specified
 * text file.
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
     *            name of an immutable text file.
     * @return
     */
    public static LineServer create(String fileName) {
        TextFile textFile = new TextFile(fileName);
        return new LineServer(textFile);
    }

    /**
     * @param textFile
     *            name of an immutable text file.
     */
    public LineServer(TextFile textFile) {
        this.textFile = textFile;
    }

    /**
     * Runs server.
     */
    public void run() {
        // TODO(dst), Sep 1, 2013: impl
        StdLogger.info(textFile.getLine(1));
    }

}
