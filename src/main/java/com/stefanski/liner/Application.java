package com.stefanski.liner;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.stefanski.liner.server.Server;

import static java.util.Optional.empty;
import static java.util.Optional.of;

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
    private Server server;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        getFileName(args).ifPresent(
                fileName -> runServer(fileName)
        );
    }

    private Optional<String> getFileName(String... args) {
        if (args.length != 1) {
            log.error(usage());
            return empty();
        }
        return of(args[0]);
    }

    private void runServer(String fileName) {
        try {
            server.run(fileName);
        } catch (Exception e) {
            log.error("Cannot run server: ", e);
            System.exit(1);
        }
    }

    private static String usage() {
        return String.format("Usage: %s fileName", Application.class.getSimpleName());
    }
}
