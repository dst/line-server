package com.stefanski.lineserver.server.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.stefanski.lineserver.server.cmd.Command;
import com.stefanski.lineserver.server.cmd.CommandParser;
import com.stefanski.lineserver.server.cmd.CommandParserException;
import com.stefanski.lineserver.server.cmd.EmptyCommand;
import com.stefanski.lineserver.server.resp.Response;
import com.stefanski.lineserver.util.StdLogger;

/**
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class SocketCommunication implements Communication {

    private final CommandParser parser;
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public SocketCommunication(Socket socket) throws IOException {
        parser = new CommandParser();
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Command receiveCommand() {
        try {
            String line = reader.readLine();
            return parser.parseCmd(line);
        } catch (IOException | CommandParserException e) {
            StdLogger.error("Cannot create new command: " + e);
            return new EmptyCommand();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendResponse(Response resp) throws IOException {
        resp.write(writer);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }
}
