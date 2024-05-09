import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    /**
     * <p>TCP/IP 기반 Redis 서버 엔트리 포인트</p>
     *
     */
    public static void main(String[] args) {
        ServerSocket serverSocket = null; //
        Socket clientSocket = null;
        int port = 6379;
        printLogo();
        try {
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.
            clientSocket = serverSocket.accept();
            client(clientSocket);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    public static void printLogo() {
        Logger.info("Hoon Redis");
        Logger.info("listening on port 6379 (tcp)");
    }

    public static void client(Socket clientSocket) {
        Logger.info(clientSocket + "is connected");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream())) {
            loop(br, printWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loop(BufferedReader br, PrintWriter pw) throws IOException {
        String data;
        while ((data = br.readLine()) != null) {
            Logger.info(data);
            pw.print("+PONG\r\n");
            pw.flush();
        }
    }
}
