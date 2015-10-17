package com.stefanski.liner.server;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.file.TextFileFactory;
import com.stefanski.liner.server.communication.CommunicationDetector;
import com.stefanski.liner.server.communication.TCPCommunicationDetector;

/**
 * A factory for creating servers.
 * 
 * @author Dariusz Stefanski
 * @date Sep 3, 2013
 */
public class LinerServerFactory {

    /*
     * Determines how many simultaneous clients the server can handle.
     */
    private static final int SIMULTANEOUS_CLIENTS_LIMIT = 100;

    private LinerServerFactory() {
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
    public static LinerServer createServer(String fileName) throws TextFileException {
        CommunicationDetector detector = new TCPCommunicationDetector();
        TextFile textFile = TextFileFactory.createFromFile(fileName);

        return new LinerServer(SIMULTANEOUS_CLIENTS_LIMIT, detector, textFile);
    }
}
