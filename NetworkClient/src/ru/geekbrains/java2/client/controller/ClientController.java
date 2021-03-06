package ru.geekbrains.java2.client.controller;

import ru.geekbrains.java2.client.model.NetworkService;
import ru.geekbrains.java2.client.view.AuthDialog;
import ru.geekbrains.java2.client.view.ClientChat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static ru.geekbrains.java2.client.Command.*;

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
        authDialog.setVisible(true);
        networkService.setSuccessfulAuthEvent(nickName -> {
            ClientController.this.setNickName(nickName);
            ClientController.this.openChat();
        });
    }

    private void openChat() {
        authDialog.dispose();
        networkService.setMessageHandler(clientChat::appendMsg);
        clientChat.setTitle(getNickName() + " chat");
        clientChat.setVisible(true);
    }

    private void connectToServer() throws IOException {
        try {
            networkService.connect(this);
        } catch (IOException e) {
            System.err.println("Не удалось установить соединение с сервером");
            throw e;
        }

    }

    public void sendAuthMsg(String login, String password) throws IOException {
        networkService.sendCommand(authCommand(login, password));
    }

    public void sendChangeNickMsg(String newNickName, String oldNickName) throws IOException {
        networkService.sendCommand(updateNickCommand(newNickName, oldNickName));
    }


    public void sendPrivateMsg(String receiver, String msg) {
        try {
            networkService.sendCommand(privateMsgCommand(receiver, msg));
        } catch (IOException e) {
            showErrorMsg("Не удалось отправить сообщение для: " + receiver);
            e.printStackTrace();
        }
    }

    public void sendMsgToAll(String msg) {
        try {
            networkService.sendCommand(broadcastMsgCommand(msg));
        } catch (IOException e) {
            clientChat.showError("Не удалось отправить сообщение!");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (authDialog.isActive()) {
            authDialog.dispose();
        }
        networkService.close();
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void showErrorMsg(String errorMsg) {
        if (clientChat.isActive()) {
            clientChat.showError(errorMsg);
        } else if (authDialog.isActive()) {
            authDialog.showError(errorMsg);

        }
        System.err.println(errorMsg);
    }

    public void showNewNickName(String newNickName) {
//        clientChat.isActive()
        clientChat.setTitle(newNickName + " chat");
        clientChat.showNewNickNameMsg("Ваш новый nickName: " + newNickName);

    }

    public void updateUsersList(List<String> users) {
        users.remove(nickName);
        users.add(0, "");
        clientChat.updateUsers(users);
    }

    public void writeMsgToHistory(String msg) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("history_" + networkService.getLogin(), true))) {
            bufferedWriter.write(msg + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
