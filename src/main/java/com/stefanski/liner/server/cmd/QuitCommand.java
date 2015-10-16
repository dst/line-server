package com.stefanski.liner.server.cmd;

import com.stefanski.liner.server.resp.EmptyResponse;
import com.stefanski.liner.server.resp.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * A request to disconnect client.
 * 
 * @author Dariusz Stefanski
 * @date Sep 12, 2013
 */
@Slf4j
public class QuitCommand implements Command {

    private static QuitCommand INSTANCE = new QuitCommand();

    public static QuitCommand getInstance() {
        return INSTANCE;
    }

    private QuitCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(CommandContext ctx) {
        log.info("Disconnecting client");
        ctx.getClientHandler().quit();
        return EmptyResponse.getInstance();
    }
}
