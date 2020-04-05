package ru.geekbrains.java2.server.client;

import ru.geekbrains.java2.client.Command;
import ru.geekbrains.java2.client.CommandType;
import ru.geekbrains.java2.client.command.AuthCommand;
import ru.geekbrains.java2.client.command.BroadcastMsgCommand;
import ru.geekbrains.java2.client.command.PrivateMsgCommand;
import ru.geekbrains.java2.client.command.UpdateNickCommand;
import ru.geekbrains.java2.server.NetworkServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientHandler {
    private final NetworkServer networkServer;
    private final Socket clientSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

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
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            new Thread(() -> {
                try {
                    Thread timeOutAuth = new Thread(() -> {
                        try {
                            Thread.sleep(TimeUnit.SECONDS.toMillis(120));
                            if (nickName == null) {
                                System.err.println("Превышение таймаута на аутентификацию клиента");
                                Command authErrorCommand = Command.errorCommand("Превышение таймаута на аутентификацию");
                                sendMsg(authErrorCommand);
                                clientSocket.close();
                            }
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                    timeOutAuth.setDaemon(true);
                    timeOutAuth.start();

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
        try {
            networkServer.unSubscribe(this);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMsg() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case END:
                    System.out.println("Получена команда завершения: " + command.getType());
                    return;
                case PRIVATE_MESSAGE: {
                    PrivateMsgCommand commandData = (PrivateMsgCommand) command.getData();
                    String receiver = commandData.getReceiver();
                    String msg = commandData.getMsg();
                    networkServer.sendMsg(receiver, Command.msgCommand(nickName, msg));
                    break;
                }
                case BROADCAST_MESSAGE: {
                    BroadcastMsgCommand commandData = (BroadcastMsgCommand) command.getData();
                    String msg = commandData.getMsg();
                    networkServer.broadCastMsg(Command.msgCommand(nickName, msg), this);
                    break;
                }
                case UPDATE_NICK_NAME:
                    doUpdateNickCommand(command);
                    break;
                default:
                    System.out.println("Получена неизвестная команда чтения: " + command.getType());
            }
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Неизвестный тип объекта от клиента!";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMsg(Command.errorCommand(errorMessage));
            return null;
        }
    }

    private void authUser() throws IOException {

        while (true) {
            Command command = readCommand();

            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                boolean successfulAuth = doAuthCommand(command);
                if (successfulAuth) {
                    return;
                }
            } else {
                System.err.println("Получена неизвестная команда аутентификации: " + command.getType());
            }
        }
    }

    private boolean doAuthCommand(Command command) throws IOException {
        AuthCommand commandData = (AuthCommand) command.getData();
        String login = commandData.getLogin();
        String password = commandData.getPassword();
        String username = networkServer.getAuthService().getUserNameByLoginAndPassword(login, password);
        if (username == null) {
            Command authErrorCommand = Command.authErrorCommand("Отсутствует учетная запись по данному логину и паролю!");
            sendMsg(authErrorCommand);
            return false;
        } else if (networkServer.isNicknameBusy(username)) {
            Command authErrorCommand = Command.authErrorCommand("Данный пользователь уже авторизован!");
            sendMsg(authErrorCommand);
            return false;
        } else {
            nickName = username;
            String message = nickName + " зашел в чат!";
            networkServer.broadCastMsg(Command.msgCommand(null, message), this);
            commandData.setUsername(nickName);
            sendMsg(command);
            networkServer.subscribe(this);
            networkServer.fillHistoryChat(this);
            return true;
        }
    }

    private void doUpdateNickCommand(Command command) throws IOException {
        UpdateNickCommand commandData = (UpdateNickCommand) command.getData();
        String newNickName = commandData.getNewNickName();
        String oldNickName = commandData.getOldNickName();

        int successfulUpdate = networkServer.getAuthService().updateUserNickName(newNickName, oldNickName);
        if (successfulUpdate == 0) {
            Command authErrorCommand = Command.authErrorCommand(String.format("Пользователь с nickName = %s уже существует%n", newNickName));
            sendMsg(authErrorCommand);
        } else {

            String message = oldNickName + " сменил nickName! Новый nickName: " + newNickName;
            networkServer.broadCastMsg(Command.msgCommand(null, message), this);
            networkServer.unSubscribe(this);
            nickName = newNickName;
            sendMsg(command);
            networkServer.subscribe(this);
        }
    }

    public void sendMsg(Command command) throws IOException {
        out.writeObject(command);
    }

    public String getNickName() {
        return nickName;
    }
}
