package com.stefanski.lineserver;

/**
 * Very simple logger. It logs to standard output and error.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
public final class StdLogger {

    public static void info(String msg) {
        System.out.println(msg);
    }

    public static void error(String msg) {
        System.err.println(msg);
    }
}
