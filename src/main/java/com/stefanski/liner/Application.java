package com.stefanski.liner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.Server;
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
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    Server server;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String fileName = getFileName(args);
        runServer(fileName);
    }

    private String getFileName(String... args) {
        if (args.length != 1) {
            log.error(usage());
            System.exit(1);
        }
        return args[0];
    }

    private void runServer(String fileName) {
        try {
            server.run(fileName);
        } catch (TextFileException | CommunicationException e) {
            log.error("Cannot run server: ", e);
            System.exit(1);
        }
    }

    private static String usage() {
        return String.format("Usage: %s fileName", Application.class.getSimpleName());
    }
}
