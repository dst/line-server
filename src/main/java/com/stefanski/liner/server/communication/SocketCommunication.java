package com.stefanski.liner.server.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;

import com.stefanski.liner.server.command.Command;
import com.stefanski.liner.server.command.parser.CommandParserException;
import com.stefanski.liner.server.command.parser.CommandParserService;
import com.stefanski.liner.server.command.EmptyCommand;
import com.stefanski.liner.server.resp.Response;

/**
 * A communication is done via a socket.
 *
 * @author Dariusz Stefanski
 * @since Sep 12, 2013
 */
@Slf4j
public class SocketCommunication implements Communication {

    private final Socket socket;
    private final CommandParserService parser;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public SocketCommunication(Socket socket, CommandParserService parser) throws IOException {
        this.socket = socket;
        this.parser = parser;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public Command receiveCommand() {
        try {
            String line = reader.readLine();
            if (line == null) {
                throw new CommunicationException("Communication channel was closed");
            }
            return parser.parseCmd(line);
        } catch (IOException | CommandParserException e) {
            log.error("Cannot create new command: ", e);
            return EmptyCommand.getInstance();
        }

    }

    @Override
    public void sendResponse(Response resp) {
        try {
            resp.write(writer);
        } catch (IOException e) {
            throw new CommunicationException("Cannot write response", e);
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }
}
