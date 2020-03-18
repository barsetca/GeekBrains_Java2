package ru.geekbrains.java2.client.model;

import ru.geekbrains.java2.client.controller.AuthEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class NetworkService {

    private final String host;
    private final int port;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private Consumer<String> messageHandler;
    private AuthEvent successfulAuthEvent;
    private String nickName;

    public NetworkService(String host, int port) {

        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {

        socket = new Socket(host, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        runReadThread();

    }

    private void runReadThread() {
        new Thread(() -> {
            while (true) {
                try {
                    String msg = in.readUTF();
                    if (msg.startsWith("/auth")) {
                        String[] msgParts = msg.split("\\s+", 2);
                        nickName = msgParts[1];
                        successfulAuthEvent.authIsSuccessful(nickName);
                    } else if (messageHandler != null) {
                        messageHandler.accept(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Процесс чтения прерван!");
                    return;
                }
            }
        }).start();
    }

    public void sendAuthMsg(String login, String password) throws IOException {
        out.writeUTF(String.format("/auth %s %s", login, password));
    }

    public void sendMsg(String msg) throws IOException {
        out.writeUTF(msg);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMessageHandler(Consumer<String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setSuccessfulAuthEvent(AuthEvent successfulAuthEvent) {
        this.successfulAuthEvent = successfulAuthEvent;
    }


}
