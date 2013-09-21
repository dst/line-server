package com.stefanski.lineserver.server;

import com.stefanski.lineserver.file.IndexedTextFile;
import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.file.TextFileException;
import com.stefanski.lineserver.server.comm.CommunicationDetector;
import com.stefanski.lineserver.server.comm.TCPCommunicationDetector;

/**
 * A factory for creating line servers.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class LineServerFactory {

    /*
     * Determines how many simultaneous clients the server can handle.
     */
    private static final int SIMULTANEOUS_CLIENTS_LIMIT = 100;

    private LineServerFactory() {
    }

    /**
     * Creates new line server associated with specified file.
     * 
     * @param fileName
     *            The name of an immutable text file
     * @return A new line server
     * @throws TextFileException
     *             If there is critical problem with creating TextFile
     */
    public static LineServer createServer(String fileName) throws TextFileException {
        CommunicationDetector detector = new TCPCommunicationDetector();
        TextFile textFile = IndexedTextFile.createFromFile(fileName);

        return new LineServer(SIMULTANEOUS_CLIENTS_LIMIT, detector, textFile);
    }
}
