package com.stefanski.liner.server.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * *It listens for TCP connections on specified port.
 * 
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
@Slf4j
@Component
public class TCPCommunicationDetector implements CommunicationDetector {
    /**
     * Detector listens for connections on this port.
     */
    public static final int TCP_PORT = 6789;

    /**
     * Socket for accepting clients.
     */
    private ServerSocket serverSocket;

    @Override
    public void start() throws CommunicationException {
        log.info("Start detecting clients on port {}", TCP_PORT);

        try {
            serverSocket = new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            throw new CommunicationException("Could not listen on port: " + TCP_PORT, e);
        }
    }

    @Override
    public void stop() throws CommunicationException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new CommunicationException("I/O exception when closing server socket: ", e);
        }
    }

    @Override
    public Communication acceptNextClient() throws CommunicationException {
        try {
            Socket clientSocket = serverSocket.accept();
            return SocketCommunication.fromSocket(clientSocket);
        } catch (IOException e) {
            throw new CommunicationException("Cannot accept next client", e);
        }
    }
}
