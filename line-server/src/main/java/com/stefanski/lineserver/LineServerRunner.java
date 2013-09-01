package com.stefanski.lineserver;

/**
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

        LineServer server = LineServer.create(fileName);
        StdLogger.info("Starting server...");
        server.run();
        StdLogger.info("Server stopped.");
    }
}
