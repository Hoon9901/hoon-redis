package client;

import log.Logger;
import server.MemoryStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class Protocol {
    private static final String DOLLAR = "$";
    private static final String ASTERISK = "*";
    private static final String PLUS = "+";
    private static final String NULL_BULK_STRING = "$-1\r\n";

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
        } else if (command == Command.SET) {
            if (args.size() < 2) {
                throw new RuntimeException("Unknown SET process");
            }
            if (args.size() == 4 && args.get(2).equals("px")) {
                MemoryStorage.save(args.get(0), args.get(1), Integer.valueOf(args.get(3)));
            } else {
                MemoryStorage.save(args.get(0), args.get(1));
            }
            sb.append(PLUS);
            sb.append(ResponseKeyword.OK);
            sb.append("\r\n");
        } else if (command == Command.GET) {
            if (args.size() != 1) {
                throw new RuntimeException("Unknown GET process");
            }
            String value = MemoryStorage.get(args.get(0));
            // TODO NULL 체크를 하드코딩에서 유연하게 변경
            if (value == null) {
                sb.append(NULL_BULK_STRING);
                return sb.toString();
            }
            sb.append(DOLLAR);
            sb.append(getStringLength(value));
            sb.append("\r\n");
            sb.append(value);
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
        PING, ECHO,
        SET, GET,
        UNKNOWN;

        public static Command from(String s) {
            try {
                return Command.valueOf(s);
            } catch (IllegalArgumentException e) {
                return Command.UNKNOWN;
            }
        }
    }

    enum ResponseKeyword {
        PONG, OK;
    }
}
