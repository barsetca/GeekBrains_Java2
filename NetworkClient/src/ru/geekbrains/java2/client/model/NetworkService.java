package ru.geekbrains.java2.client.model;

import ru.geekbrains.java2.client.Command;
import ru.geekbrains.java2.client.CommandType;
import ru.geekbrains.java2.client.command.*;
import ru.geekbrains.java2.client.controller.AuthEvent;
import ru.geekbrains.java2.client.controller.ClientController;
import ru.geekbrains.java2.client.controller.MsgHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class NetworkService {

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private ClientController controller;

    private MsgHandler msgHandler;
    private AuthEvent successfulAuthEvent;
    private String nickName;
    private String login;

    public NetworkService(String host, int port) {

        this.host = host;
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void connect(ClientController controller) throws IOException {
        this.controller = controller;
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        runReadThread();

    }

    private void runReadThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Command command = (Command) in.readObject();
                    if (command == null) {
                        continue;
                    }
                    switch (command.getType()) {
                        case AUTH: {
                            AuthCommand commandData = (AuthCommand) command.getData();
                            nickName = commandData.getUsername();
                            login = commandData.getLogin();
                            successfulAuthEvent.authIsSuccessful(nickName);

                            break;
                        }
                        case UPDATE_NICK_NAME: {
                            UpdateNickCommand commandData = (UpdateNickCommand) command.getData();
                            nickName = commandData.getNewNickName();
                            controller.setNickName(nickName);
                            controller.showNewNickName(nickName);
                            break;
                        }
                        case MESSAGE: {
                            MsgCommand commandData = (MsgCommand) command.getData();
                            if (msgHandler != null) {
                                String msg = commandData.getMsg();
                                String userName = commandData.getUsername();
                                if (userName != null) {
                                    msg = userName + ": " + msg;
                                }
                                msgHandler.handle(msg);
                                controller.writeMsgToHistory(msg);
                            }
                            break;
                        }
                        case HISTORY_LINES: {
                            HistoryMsgCommand commandData = (HistoryMsgCommand) command.getData();
                            if (msgHandler != null) {
                                String msg = commandData.getMsg().trim();

                                if (!msg.isEmpty()) {
                                    msgHandler.handle(msg);
                                }
                            }
                            break;
                        }
                        case AUTH_ERROR:
                        case ERROR: {
                            ErrorCommand commandData = (ErrorCommand) command.getData();
                            controller.showErrorMsg(commandData.getErrorMsg());
                            if (command.getType() == CommandType.ERROR) {
                                controller.shutdown();
                            }
                            break;
                        }
                        case UPDATE_USERS_LIST:
                            UpdateUsersListCommand commandData = (UpdateUsersListCommand) command.getData();
                            List<String> users = commandData.getUsers();
                            controller.updateUsersList(users);
                            break;
                        default:
                            System.out.println("Получена неизвестная команда чтения: " + command.getType());
                    }

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Процесс работы в чате прерван!");
                    return;
                }
            }
        }).start();
    }


    public void sendCommand(Command command) throws IOException {
        out.writeObject(command);
    }

    public void setMessageHandler(MsgHandler msgHandler) {
        this.msgHandler = msgHandler;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSuccessfulAuthEvent(AuthEvent successfulAuthEvent) {
        this.successfulAuthEvent = successfulAuthEvent;
    }
}
