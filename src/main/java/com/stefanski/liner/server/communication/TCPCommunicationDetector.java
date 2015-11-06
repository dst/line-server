package com.stefanski.liner.server.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stefanski.liner.server.command.CommandParserService;

/**
 * *It listens for TCP connections on specified port.
 *
 * @author Dariusz Stefanski
 * @since Sep 21, 2013
 */
@Slf4j
@Component
class TCPCommunicationDetector implements CommunicationDetector {

    private final int port;
    private final CommandParserService parser;

    /**
     * Socket for accepting clients.
     */
    private ServerSocket serverSocket;

    @Autowired
    TCPCommunicationDetector(@Value("${server.portNr}") int port,
                             CommandParserService parser) {
        this.port = port;
        this.parser = parser;
    }

    @Override
    public void start() {
        log.info("Start detecting clients on port {}", port);

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new CommunicationException("Could not listen on port: " + port, e);
        }
    }

    @Override
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new CommunicationException("I/O exception when closing server socket: ", e);
        }
    }

    @Override
    public Communication acceptNextClient() {
        try {
            Socket clientSocket = serverSocket.accept();
            return new SocketCommunication(clientSocket, parser);
        } catch (IOException e) {
            throw new CommunicationException("Cannot accept next client", e);
        }
    }
}
