package com.stefanski.liner.server.command.parser;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stefanski.liner.server.command.Command;

/**
 * Parses commands from String.
 *
 * @author Dariusz Stefanski
 * @since Sep 12, 2013
 */
@Service
@Slf4j
public class CommandParserService {

    private final List<CommandParser> parsers;

    @Autowired
    public CommandParserService(List<CommandParser> parsers) {
        this.parsers = parsers;
    }

    /**
     * Parses a given line to a command.
     *
     * @throws CommandParserException
     */
    public Command parse(String line) {
        log.trace("Paring command: {}", line);
        assert line != null;
        return parsers.stream()
                .filter(parser -> parser.isApplicableTo(line))
                .findAny()
                .map(parser -> parser.parse(line))
                .orElseThrow(() -> new CommandParserException("Unknown command: " + line));
    }
}
