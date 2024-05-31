package server;

import log.Logger;

import java.io.*;
import java.net.Socket;

public class ReplicaTask implements Runnable {

    private final Socket masterSocket;

    public ReplicaTask(Socket masterSocket) {
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
        if (!sendPingToMaster(br, pw)) {
            Logger.error("Failed send ping to master");
            return;
        }

        if (!sendReplconfToMaster(br, pw)) {
            Logger.error("Failed send REPLCONF to master");
            return;
        }

    }

    private boolean sendPingToMaster(BufferedReader br, PrintWriter pw) throws IOException {
        // 1. send a PING to master
        pw.print("*1\r\n$4\r\nPING\r\n");
        pw.flush();
        String receive = br.readLine();
        return receive.equals("+PONG");
    }

    private boolean sendReplconfToMaster(BufferedReader br, PrintWriter pw) throws IOException {
        pw.print("*3\r\n$8\r\nREPLCONF\r\n$14\r\nlistening-port\r\n$4\r\n%s\r\n".formatted(Configuration.port));
        pw.flush();
        String response = br.readLine();
        if (!response.equals("+OK")) {
            return false;
        }
        pw.print("*3\r\n$8\r\nREPLCONF\r\n$4\r\ncapa\r\n$6\r\npsync2\r\n");
        pw.flush();
        response = br.readLine();
        return response.equals("+OK");
    }
}
