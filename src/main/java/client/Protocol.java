package client;

import log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class Protocol {
    private static final String DOLLAR = "$";
    private static final String ASTERISK = "*";
    public static final String PLUS = "+";

    public static void process(final BufferedReader br, final PrintWriter pw) throws IOException {
        String response = read(br);
        send(pw, response);
    }

    private static String read(final BufferedReader br) throws IOException {
        final String input = br.readLine();
        if (input == null) {
            return null;
        }

        if (input.startsWith(ASTERISK)) {
            int size = Integer.parseInt(input.substring(1));
            return processMultiArgs(br, size);
        } else {
            throw new RuntimeException("Unknown reply: " + input);
        }
    }

    private static String processMultiArgs(final BufferedReader br, int size) throws IOException {
        // Command Parsing
        int byteLength = getStringLength(br.readLine());

        String arg = br.readLine().toUpperCase();
        Command command = null;
        if (arg.length() == byteLength) {
            command = Command.from(arg);
        }

        // Args Parsing
        List<String> args = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            byteLength = getStringLength(br.readLine());

            arg = br.readLine();
            if (arg.length() == byteLength) {
                args.add(arg);
            }
        }

        // Process Command
        return processCommand(command, args);
    }

    private static int getStringLength(String len) {
        if (len.startsWith(DOLLAR)) {
            return Integer.parseInt(len.substring(1));
        }
        return len.length();
    }

    private static String processCommand(Command command, List<String> args) {
        // TODO Command Pattern
        StringBuilder sb = new StringBuilder();

        if (command == Command.PING) {
            sb.append(PLUS);
            sb.append(ResponseKeyword.PONG);
            sb.append("\r\n");
        } else if (command == Command.ECHO) {
            if (args.isEmpty()) {
                throw new RuntimeException("Unknown ECHO process");
            }
            String echo = args.get(0);
            sb.append(DOLLAR);
            sb.append(getStringLength(echo));
            sb.append("\r\n");
            sb.append(echo);
            sb.append("\r\n");
        } else if (command == Command.UNKNOWN) {
            throw new RuntimeException("unknown command");
        }

        return sb.toString();
    }

    private static void send(final PrintWriter printWriter, String response) {
        if (response == null) {
            return;
        }
        Logger.info(String.format("Send message: %s", response));
        printWriter.print(response);
        printWriter.flush();
    }

    enum Command {
        PING, ECHO, UNKNOWN;

        public static Command from(String s) {
            try {
                return Command.valueOf(s);
            } catch (IllegalArgumentException e) {
                return Command.UNKNOWN;
            }
        }
    }

    enum ResponseKeyword {
        PONG;
    }
}
