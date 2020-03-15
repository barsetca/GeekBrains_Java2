package lesson06.singleclient;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ChatClient() {
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        new Thread(() -> {
            receiveMsg(in);
            closeConnection();
            System.out.println("Сервер закрыл соединение.");
            System.exit(0);
        }).start();

        new Thread(() -> {
            sendMsg(out);
            closeConnection();
            System.out.println("Вы закрыли соединение");
            //System.exit(0);
        }).start();
    }

    private void receiveMsg(DataInputStream in) {
        while (true) {
            try {
                String serverMsg = in.readUTF();
                System.out.println(serverMsg);
            } catch (Exception e) {
                break;
            }
        }
    }

    private void sendMsg(DataOutputStream out) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String clientMsg;
            while ((clientMsg = reader.readLine()) != null) {
                if (clientMsg.trim().isEmpty()) {
                    continue;
                }
                if (clientMsg.equalsIgnoreCase("exit")) {
                    break;
                }
                out.writeUTF("Клиент: " + clientMsg);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        new Thread(ChatClient::new).start();
    }
}
