package com.stefanski.liner.server.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stefanski.liner.server.command.parser.CommandParserAggregator;

/**
 * *It listens (detects) for TCP connections on specified port.
 *
 * @author Dariusz Stefanski
 * @since Sep 21, 2013
 */
@Slf4j
@Component
public class TCPCommunicationDetector {

    private final int port;
    private final CommandParserAggregator parser;

    /**
     * Socket for accepting clients.
     */
    private ServerSocket serverSocket;

    @Autowired
    TCPCommunicationDetector(@Value("${server.portNr}") int port,
                             CommandParserAggregator parser) {
        this.port = port;
        this.parser = parser;
    }

    /**
     * Prepares for detecting clients.
     *
     * @throws CommunicationException
     */
    public void start() {
        log.info("Start detecting clients on port {}", port);

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new CommunicationException("Could not listen on port: " + port, e);
        }
    }

    /**
     * Stops a detection.
     *
     * @throws CommunicationException
     */
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new CommunicationException("I/O exception when closing server socket: ", e);
        }
    }

    /**
     * After accepting the next client, it returns a communication channel to this client.
     *
     * @return a communication between client and server
     * @throws CommunicationException
     */
    public Communication acceptNextClient() {
        try {
            Socket clientSocket = serverSocket.accept();
            return new SocketCommunication(clientSocket, parser);
        } catch (IOException e) {
            throw new CommunicationException("Cannot accept next client", e);
        }
    }
}
