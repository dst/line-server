package com.stefanski.liner;

import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.LinerServer;
import com.stefanski.liner.server.LinerServerFactory;
import com.stefanski.liner.server.comm.CommunicationException;
import com.stefanski.liner.util.StdLogger;

/**
 * Starts server.
 * 
 * It takes a single command-line parameter which is the name of the file to serve
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
public class ServerRunner {

    private ServerRunner() {
    }

    public static void main(String[] args) {
        String fileName = getFileName(args);
        runServer(fileName);
    }

    private static String getFileName(String[] args) {
        if (args.length != 1) {
            StdLogger.error(usage());
            System.exit(1);
        }
        return args[0];
    }

    private static void runServer(String fileName) {
        try {
            LinerServer server = LinerServerFactory.createServer(fileName);
            server.run();
        } catch (TextFileException | CommunicationException e) {
            StdLogger.error("Cannot run server: " + e);
            System.exit(1);
        }
    }

    private static String usage() {
        return String.format("Usage: %s fileName", ServerRunner.class.getSimpleName());
    }
}
