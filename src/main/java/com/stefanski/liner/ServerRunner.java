package com.stefanski.liner;

import lombok.extern.slf4j.Slf4j;

import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.LinerServer;
import com.stefanski.liner.server.LinerServerFactory;
import com.stefanski.liner.server.communication.CommunicationException;

/**
 * Starts server.
 * 
 * It takes a single command-line parameter which is the name of the file to serve
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
@Slf4j
public class ServerRunner {

    private ServerRunner() {
    }

    public static void main(String[] args) {
        String fileName = getFileName(args);
        runServer(fileName);
    }

    private static String getFileName(String[] args) {
        if (args.length != 1) {
            log.error(usage());
            System.exit(1);
        }
        return args[0];
    }

    private static void runServer(String fileName) {
        try {
            LinerServer server = LinerServerFactory.createServer(fileName);
            server.run();
        } catch (TextFileException | CommunicationException e) {
            log.error("Cannot run server: ", e);
            System.exit(1);
        }
    }

    private static String usage() {
        return String.format("Usage: %s fileName", ServerRunner.class.getSimpleName());
    }
}
