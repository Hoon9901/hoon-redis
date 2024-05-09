import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    /**
     * <p>TCP/IP 기반 Redis 서버 엔트리 포인트</p>
     *
     */
    public static void main(String[] args) {
        System.out.println("Hoon Redis");
        System.out.println("listening on port 6379 (tcp)");
        ServerSocket serverSocket = null; //
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.
            clientSocket = serverSocket.accept();
            System.out.println(clientSocket.getLocalAddress() + " is connected");
            try(InputStream inputStream = clientSocket.getInputStream(); OutputStream outputStream = clientSocket.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int dataSize = 0;
                while((dataSize = inputStream.read(buffer)) != -1) {
                    String message = new String(buffer, 0, dataSize);
                    System.out.println(message);

                    PrintWriter writer = new PrintWriter(outputStream);
                    writer.print("+PONG\r\n");
                    writer.flush();
                }
            }
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
}
