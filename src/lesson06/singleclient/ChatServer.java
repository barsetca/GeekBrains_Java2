package lesson06.singleclient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void main(String[] args) {
        Socket socket;

        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен, ожидаем подключения ...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Соединение установлено!\n" + "Для выхода из чата введите команду: exit");

            new Thread(() -> {
                try {
                    sendMsg();
                    serverSocket.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Сервер завершил работу. Для возобновления работы перезапустите сервер.");
                System.exit(0);
            }).start();

            new Thread(() -> {
                try {
                    receiveMsg();
                } catch (Exception ex) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Клиент " + Thread.currentThread().getName() + " вышел из чата");
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveMsg() throws Exception {
        while (true) {
            String clientMsg = in.readUTF();
            out.writeUTF(clientMsg);
            System.out.println(clientMsg);
        }
    }

    private static void sendMsg() throws Exception {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String serverMsg;
            while ((serverMsg = reader.readLine()) != null) {
                if (serverMsg.trim().isEmpty()) {
                    continue;
                }
                if (serverMsg.equalsIgnoreCase("exit")) {
                    break;
                }
                String msg = "Сервер: " + serverMsg;
                out.writeUTF(msg);
                System.out.println(msg);
            }
        }
    }
}
