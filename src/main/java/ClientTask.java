import java.io.*;
import java.net.Socket;

public class ClientTask implements Runnable {

    private final Socket clientSocket;

    public ClientTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        Logger.info(clientSocket + " is connected");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            loop(br, pw);
        } catch (IOException e) {
            Logger.error("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                    Logger.info(clientSocket + " is close");
                }
            } catch (IOException e) {
                Logger.error("IOException: " + e.getMessage());
            }
        }
    }

    private void loop(BufferedReader br, PrintWriter pw) throws IOException {
        String data;
        while ((data = br.readLine()) != null) {
            Logger.info(data);
            if (data.equalsIgnoreCase("PING")) {
                pw.print("+PONG\r\n");
                pw.flush();
            }
        }
    }

}
