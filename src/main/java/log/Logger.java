package log;

import java.time.LocalDateTime;

public class Logger {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static void error(String message) {
        log(message, "ERROR");
    }

    public static void info(String message) {
        log(message, "INFO");
    }

    private static void log(String message, String type) {
        StringBuilder logMessage = new StringBuilder();
        time(logMessage);
        type(logMessage, type);
        message(logMessage, message);
        System.out.println(logMessage);
    }

    private static void type(StringBuilder logMessage, String type) {
        switch (type) {
            case "INFO" -> logMessage.append(ANSI_GREEN);
            case "ERROR" -> logMessage.append(ANSI_RED);
        }

        logMessage.append(" [");
        logMessage.append(type);
        logMessage.append("] ");
    }

    private static void time(StringBuilder logMessage) {
        logMessage.append(ANSI_YELLOW);
        logMessage.append(LocalDateTime.now());
    }

    private static void message(StringBuilder logMessage, String message) {
        logMessage.append(ANSI_RESET);
        logMessage.append(message);
    }

}
