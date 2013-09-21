package com.stefanski.lineserver.server.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.stefanski.lineserver.util.StdLogger;

/**
 * *It listens for TCP connections on port 10497.
 * 
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
 */
public class TCPCommunicationDetector implements CommunicationDetector {
    /**
     * Detector listens for connections on this port.
     */
    public static final int TCP_PORT = 10497;

    /**
     * Socket for accepting clients.
     */
    private ServerSocket serverSocket;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws CommunicationException {
        StdLogger.info(String.format("Start detecting clients on port %s...", TCP_PORT));

        try {
            serverSocket = new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            throw new CommunicationException("Could not listen on port: " + TCP_PORT, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() throws CommunicationException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new CommunicationException("I/O exception when closing server socket: ", e);
        }
    }

    /**
     * {@inheritDoc}
     */
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
