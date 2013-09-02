package com.stefanski.lineserver;

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

    public static void main(String[] args) {
        if (args.length != 1) {
            StdLogger.error("File name not specified");
            System.exit(1);
        }
        String fileName = args[0];

        LineServer server = null;
        try {
            server = LineServer.create(fileName);
        } catch (LineServerException e) {
            StdLogger.error("Cannot create server: " + e);
            System.exit(1);
        }

        server.run();
    }
}
