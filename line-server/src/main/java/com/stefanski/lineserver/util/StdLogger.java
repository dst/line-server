package com.stefanski.lineserver.util;

/**
 * Very simple logger. It logs to standard output and error.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
public final class StdLogger {

    private StdLogger() {
    }

    public static void info(String msg) {
        System.out.println(formatMsg(msg));
    }

    public static void error(String msg) {
        System.err.println(formatMsg(msg));
    }

    private static String formatMsg(String msg) {
        return String.format("%d %s", System.currentTimeMillis(), msg);
    }
}
