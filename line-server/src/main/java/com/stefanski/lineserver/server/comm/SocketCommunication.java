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
 * A communication is done via socket.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
public class SocketCommunication implements Communication {

    private final Socket socket;
    private final CommandParser parser;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public static SocketCommunication fromSocket(Socket socket) throws IOException {
        return new SocketCommunication(socket, new CommandParser());
    }

    public SocketCommunication(Socket socket, CommandParser parser) throws IOException {
        this.socket = socket;
        this.parser = parser;
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
            return EmptyCommand.getInstance();
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
