package ru.geekbrains.java2.server.client;

import ru.geekbrains.java2.server.NetworkServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final NetworkServer networkServer;
    private final Socket clientSocket;

    private DataInputStream in;
    private DataOutputStream out;

    private String nickName;

    public ClientHandler(NetworkServer networkServer, Socket clientSocket) {

        this.networkServer = networkServer;
        this.clientSocket = clientSocket;
        //doHandle(clientSocket);
    }

    public void run() {
        doHandle(clientSocket);
    }

    private void doHandle(Socket clientSocket) {
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            new Thread(() -> {
                try {
                    authUser();
                    readMsg();
                } catch (IOException e) {
                    System.out.println("Соединение с клиентом: " + nickName + " закрыто!");
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        networkServer.unSubscribe(this);
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMsg() throws IOException {
        while (true) {
            String msg = in.readUTF();
            System.out.printf("От %s: %s%n", nickName, msg);
            if ("exit".equals(msg)) {
                return;
            }
            if (msg.startsWith("/to")) {
                String[] msgParts = msg.split("\\s+", 3);
                String nickNameReceiver = msgParts[1];
                String msgOneDirect = msgParts[2].trim();
                if (msgOneDirect.isEmpty()) {
                    continue;
                }
                networkServer.oneDirect(nickName + ": " + msgOneDirect, nickNameReceiver);
            } else {
                networkServer.broadCastMsg(nickName + ": " + msg, this);
            }
        }
    }

    private void authUser() throws IOException {
        while (true) {
            String msg = in.readUTF();
            // "/auth login password"
            if (msg.startsWith("/auth")) {
                String[] msgParts = msg.split("\\s+", 3);
                String login = msgParts[1];
                String password = msgParts[2];
                String userName = networkServer.getAuthService().getUserNameByLoginAndPassword(login, password);
                boolean isExist = true;
                if (userName == null) {
                    isExist = false;
                    sendMsg("Отсутствует учетная запись по данному логину и/или паролю");
                }
                for (ClientHandler client : networkServer.getClients()) {
                    if (client.nickName.equals(userName)) {
                        isExist = false;
                        sendMsg("Пользователь с такими данными уже за регистрирован");
                    }
                }
                if (isExist) {
                    nickName = userName;
                    networkServer.broadCastMsg(nickName + " зашел в чат!", this);
                    sendMsg("/auth " + nickName);
                    networkServer.subscribe(this);
                    break;
                }

            }
        }

    }

    public void sendMsg(String msg) throws IOException {
        out.writeUTF(msg);
    }

    public String getNickName() {
        return nickName;
    }
}
