package com.stefanski.lineserver;

import com.stefanski.lineserver.server.LineServer;
import com.stefanski.lineserver.server.LineServerException;
import com.stefanski.lineserver.server.LineServerFactory;
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

    public static void main(String[] args) {
        if (args.length != 1) {
            StdLogger.error("File name not specified");
            System.exit(1);
        }
        String fileName = args[0];

        try {
            LineServer server = LineServerFactory.createServer(fileName);
            server.run();
        } catch (LineServerException e) {
            StdLogger.error("Critical problem with line server: " + e);
            if (e.getCause() != null) {
                StdLogger.error("Cause: " + e.getCause());
            }
            System.exit(1);
        }
    }
}
