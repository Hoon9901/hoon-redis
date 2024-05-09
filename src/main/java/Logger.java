import java.time.LocalDateTime;

public class Logger {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void info(String message) {
        log(message, "INFO");
    }

    private static void log(String message, String type) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(ANSI_YELLOW);
        logMessage.append(LocalDateTime.now());
        logMessage.append(ANSI_GREEN);
        logMessage.append(" [");
        logMessage.append(type);
        logMessage.append("] ");
        logMessage.append(ANSI_RESET);
        logMessage.append(message);
        System.out.println(logMessage);
    }

}
