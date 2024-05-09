import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    /**
     * <p>TCP/IP 기반 Redis 서버 엔트리 포인트</p>
     */
    public static void main(String[] args) {
        printLogo();
        int port = 6379;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            loop(serverSocket);
        } catch (IOException e) {
            Logger.error("Server IOException: " + e.getMessage());
        }
    }

    public static void printLogo() {
        Logger.info("Hoon Redis");
        Logger.info("listening on port 6379 (tcp)");
    }

    public static void loop(ServerSocket serverSocket) throws IOException {
        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
            Client client = new Client(clientSocket);
            client.run();
        }
    }
}