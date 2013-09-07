package com.stefanski.lineserver.util;

/**
 * Very simple logger. It logs to standard output and error.
 * 
 * @author Dariusz Stefanski
 * @date Sep 1, 2013
 * 
 */
// TODO(dst), Sep 7, 2013: use logging library
public final class StdLogger {

    private static final boolean TRACE_ENABLED = false;

    private StdLogger() {
    }

    public static void info(String msg) {
        System.out.println(formatMsg(msg));
    }

    public static void error(String msg) {
        System.err.println(formatMsg(msg));
    }

    public static void trace(String msg) {
        if (isTraceEnabled()) {
            System.out.println(formatMsg(msg));
        }
    }

    public static boolean isTraceEnabled() {
        return TRACE_ENABLED;
    }

    private static String formatMsg(String msg) {
        return String.format("%d %s", System.currentTimeMillis(), msg);
    }
}
