package com.stefanski.liner.server.command;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.server.ClientHandler;

/**
 * A context needed to execute commands.
 *
 * @author Dariusz Stefanski
 * @since Sep 21, 2013
 */
public class CommandContext {

    /**
     * A command is executed by this handler.
     */
    private final ClientHandler clientHandler;

    /**
     * An immutable text file. From this file is taken specified line.
     */
    private final TextFile textFile;

    public CommandContext(ClientHandler clientHandler, TextFile textFile) {
        this.clientHandler = clientHandler;
        this.textFile = textFile;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public TextFile getTextFile() {
        return textFile;
    }
}
