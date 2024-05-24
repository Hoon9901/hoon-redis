import client.ClientTask;
import log.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    static int port = 6379;
    /**
     * <p>TCP/IP 기반 Redis 서버 엔트리 포인트</p>
     */
    public static void main(String[] args) {
        config(args);
        printLogo();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            loop(serverSocket);
        } catch (IOException e) {
            Logger.error("Server IOException: " + e.getMessage());
        }
    }

    public static void config(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--port")) {
                port = Integer.parseInt(args[i + 1]);
            }
        }
    }

    public static void printLogo() {
        Logger.info("Hoon Redis");
        Logger.info("listening on port %s (tcp)".formatted(port));
    }

    public static void loop(ServerSocket serverSocket) throws IOException {
        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
//            new Thread(new ClientTask(clientSocket)).start();
             Thread.ofVirtual().start(new ClientTask(clientSocket));
        }
    }
}