package com.stefanski.lineserver;

import com.stefanski.lineserver.file.TextFileException;
import com.stefanski.lineserver.server.LineServer;
import com.stefanski.lineserver.server.LineServerFactory;
import com.stefanski.lineserver.server.comm.CommunicationException;
import com.stefanski.lineserver.util.StdLogger;

/**
 * Starts line server.
 * 
 * It takes a single command-line parameter which is the name of the file to serve
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
public class LineServerRunner {

    private LineServerRunner() {
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
            LineServer server = LineServerFactory.createServer(fileName);
            server.run();
        } catch (TextFileException | CommunicationException e) {
            StdLogger.error("Cannot run server: " + e);
            System.exit(1);
        }
    }

    private static String usage() {
        return String.format("Usage: %s fileName", LineServerRunner.class.getSimpleName());
    }
}
