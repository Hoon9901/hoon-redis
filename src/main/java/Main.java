import client.ClientTask;
import log.Logger;
import server.Configuration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    /**
     * <p>TCP/IP 기반 Redis 서버 엔트리 포인트</p>
     */
    public static void main(String[] args) {
        config(args);
        printLogo();
        try (ServerSocket serverSocket = new ServerSocket(Configuration.port)) {
            serverSocket.setReuseAddress(true);
            loop(serverSocket);
        } catch (IOException e) {
            Logger.error("Server IOException: " + e.getMessage());
        }
    }

    public static void config(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--port")) {
                Configuration.port = Integer.parseInt(args[++i]);
            } else if (args[i].startsWith("--replicaof")) {
                String[] replicaOfArgs = args[++i].split(" ");
                Configuration.masterIp = replicaOfArgs[0];
                Configuration.masterPort = replicaOfArgs[1];
                Configuration.role = "slave";
            }
        }
    }

    public static void printLogo() {
        Logger.info("Hoon Redis");
        Logger.info("listening on port %s (tcp)".formatted(Configuration.port));
    }

    public static void loop(ServerSocket serverSocket) throws IOException {
        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
//            new Thread(new ClientTask(clientSocket)).start();
             Thread.ofVirtual().start(new ClientTask(clientSocket));
        }
    }
}