package ru.geekbrains.java2.server.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientHandler {

    private static final Logger LOGGER = LogManager.getLogger(NetworkServer.class);

    private final NetworkServer networkServer;
    private final Socket clientSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ExecutorService executorServer;

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

            executorServer = Executors.newSingleThreadExecutor();
            executorServer.execute(() -> {
                try {
                    ClientHandler.this.timeOutAuth(clientSocket);
                    ClientHandler.this.authUser();
                    ClientHandler.this.readMsg();
                } catch (IOException e) {
                    LOGGER.error("Соединение с клиентом: " + nickName + " закрыто!", e);
                } finally {
                    ClientHandler.this.closeConnection();
                }
            });
            executorServer.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void timeOutAuth(Socket clientSocket) {
        Thread timeOutAuth = new Thread(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(120));
                if (nickName == null) {
                    LOGGER.info("Превышение таймаута на аутентификацию клиента");
                    Command authErrorCommand = Command.errorCommand("Превышение таймаута на аутентификацию");
                    sendMsg(authErrorCommand);
                    clientSocket.close();
                }
            } catch (InterruptedException | IOException e) {
                LOGGER.error("Ошибка таймаута на аутентификацию клиента", e);
            }
        });
        timeOutAuth.setDaemon(true);
        timeOutAuth.start();
    }

    private void closeConnection() {
        try {
            networkServer.unSubscribe(this);
            clientSocket.close();
        } catch (IOException e) {
            LOGGER.fatal("Ошибка при закрытии соединения" + clientSocket, e);
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
                    LOGGER.info("Получена команда завершения: " + command.getType());
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
                    LOGGER.error("Получена неизвестная команда чтения: " + command.getType());
            }
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Неизвестный тип объекта от клиента!";
            LOGGER.error(errorMessage, e);
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
                LOGGER.error("Получена неизвестная команда аутентификации: " + command.getType());
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
            LOGGER.info("Отсутствует учетная запись по данному логину и паролю!");
            sendMsg(authErrorCommand);
            return false;
        } else if (networkServer.isNicknameBusy(username)) {
            Command authErrorCommand = Command.authErrorCommand("Данный пользователь уже авторизован!");
            LOGGER.info("Данный пользователь уже авторизован!");
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
