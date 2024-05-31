package server;

import log.Logger;

import java.io.*;
import java.net.Socket;

public class ReplicationTask implements Runnable {

    private final Socket masterSocket;

    public ReplicationTask(Socket masterSocket) {
        this.masterSocket = masterSocket;
    }

    @Override
    public void run() {
        Logger.info(masterSocket + " master is connected");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(masterSocket.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(masterSocket.getOutputStream()))) {
            handshake(br, pw);
        } catch (IOException e) {
            Logger.error(e.getMessage());
        } finally {
            try {
                if (masterSocket != null) {
                    masterSocket.close();
                    Logger.info(masterSocket + " is close");
                }
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    void handshake(BufferedReader br, PrintWriter pw) throws IOException {
        // 1. send a PING to master
        pw.print("*1\r\n$4\r\nPING\r\n");
        pw.flush();
        String read = br.readLine();
        System.out.println(read);
    }
}
