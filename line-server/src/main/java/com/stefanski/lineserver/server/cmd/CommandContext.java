package com.stefanski.lineserver.server.cmd;

import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.server.ClientHandler;

/**
 * A context needed to execute commands.
 * 
 * @author Dariusz Stefanski
 * @date Sep 21, 2013
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
