package ru.geekbrains.java2.client.controller;

import ru.geekbrains.java2.client.model.NetworkService;
import ru.geekbrains.java2.client.view.AuthDialog;
import ru.geekbrains.java2.client.view.ClientChat;

import javax.swing.*;
import java.io.IOException;

public class ClientController {

    private final NetworkService networkService;
    private final AuthDialog authDialog;
    private final ClientChat clientChat;
    private String nickName;

    public ClientController(String serverHost, int serverPort) {
        this.networkService = new NetworkService(serverHost, serverPort);
        this.authDialog = new AuthDialog(this);
        this.clientChat = new ClientChat(this);
    }

    public void runApp() throws IOException {
        connectToServer();
        runAuth();
    }

    private void runAuth() {
        networkService.setSuccessfulAuthEvent(nickName -> {
            ClientController.this.setNickName(nickName);
            ClientController.this.openChat();
        });
        authDialog.setVisible(true);
    }

    private void openChat() {
        authDialog.dispose();
        networkService.setMessageHandler(clientChat::appendMsg);
        clientChat.setTitle(getNickName() + " chat");
        clientChat.setVisible(true);
    }

    private void connectToServer() throws IOException {
        try {
            networkService.connect();
        } catch (IOException e) {
            System.err.println("Не удалось установить соединение с сервером");
            throw e;
        }

    }

    public void sendAuthMsg(String login, String password) throws IOException {
        networkService.sendAuthMsg(login, password);
    }

    public void sendMsg(String msg) {
        try {
            networkService.sendMsg(msg);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Не удалось отправить сообщение!");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        networkService.close();
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
