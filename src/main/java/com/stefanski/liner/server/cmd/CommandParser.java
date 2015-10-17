package com.stefanski.liner.server.cmd;

/**
 * @author Dariusz Stefanski
 * @date 18 Oct 2015
 */
public interface CommandParser {

    boolean isApplicableTo(String line);

    Command parse(String line);
}
