package client;

import log.Logger;

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
            while (!clientSocket.isClosed()) {
                Protocol.process(br, pw);
            }
        } catch (IOException e) {
            Logger.error("IOException: " + e.getMessage());
        } catch (Exception e) {
            Logger.error(e.getMessage());
        } finally {
            close();
        }
    }


    private void close() {
        try {
            if (clientSocket != null) {
                clientSocket.close();
                Logger.info(clientSocket + " is close");
            }
        } catch (IOException e) {
            Logger.error("IOException: " + e.getMessage());
        }
    }

}
