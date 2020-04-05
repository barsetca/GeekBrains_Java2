package ru.geekbrains.java2.server;


import ru.geekbrains.java2.client.Command;
import ru.geekbrains.java2.client.CommandType;
import ru.geekbrains.java2.client.command.MsgCommand;
import ru.geekbrains.java2.server.auth.AuthService;
import ru.geekbrains.java2.server.auth.BaseAuthServiceImpl;
import ru.geekbrains.java2.server.client.ClientHandler;
import ru.geekbrains.java2.server.dao.JdbcDao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NetworkServer {

    private final int port;
    //private final List<ClientHandler> clients = new ArrayList<>();
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final AuthService authService;
    private static final int LIMIT_LINE = 100;


    public NetworkServer(int port) {
        this.port = port;
        this.authService = new BaseAuthServiceImpl();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер успешно запущен на порту: " + port);
            authService.start();
            while (true) {
                System.out.println("Ожидание подключения клиента ...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключился");

                createClientHandler(clientSocket);
                // clients.add(new ClientHandler(this, clientSocket));
            }

        } catch (IOException e) {
            System.out.println("Ошибка сервера");
            JdbcDao.CloseConnectionDB();
            e.printStackTrace();
        } finally {
            authService.stop();
        }
    }

    private void createClientHandler(Socket clientSocket) {

        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.run();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public /*synchronized*/ void broadCastMsg(Command msgCommand, ClientHandler owner) throws IOException {


        if (msgCommand.getType().equals(CommandType.MESSAGE)) {
            MsgCommand msg = (MsgCommand) msgCommand.getData();
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("archiveChat.txt", true))){
            bufferedWriter.write(owner.getNickName() + ": " + msg.getMsg() + System.lineSeparator());
            }

        }

        for (ClientHandler client : clients) {
            if (!client.equals(owner)) {
                client.sendMsg(msgCommand);
            }
        }
    }

    public /*synchronized*/ void unSubscribe(ClientHandler client) throws IOException {
        clients.remove(client);
        List<String> users = getAllUserNames();
        broadCastMsg(Command.updateUsersListCommand(users), null);
    }

    public /*synchronized*/ void subscribe(ClientHandler client) throws IOException {
        clients.add(client);
        List<String> users = getAllUserNames();
        broadCastMsg(Command.updateUsersListCommand(users), null);
    }

    public List<String> getAllUserNames() {
        return clients.stream()
                .map(ClientHandler::getNickName)
                .collect(Collectors.toList());
    }

    public void sendMsg(String receiver, Command msgCommand) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getNickName().equals(receiver)) {
                client.sendMsg(msgCommand);
                break;
            }
        }
    }

    public boolean isNicknameBusy(String username) {
        for (ClientHandler client : clients) {
            if (client.getNickName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void fillHistoryChat(ClientHandler clientHandler) throws IOException {

        List<String> lines;
        Path path = Paths.get("archiveChat.txt");

        try (Stream<String> lineStream = Files.newBufferedReader(path).lines()) {

            lines = lineStream.collect(Collectors.toList());
            int lineSize = lines.size();
            int skipLine = 0;
            if (lineSize > LIMIT_LINE) {
                skipLine = lineSize - LIMIT_LINE;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = skipLine; i < lines.size(); i++) {
                sb.append(lines.get(i) + "\n");
            }
            sendMsg(clientHandler.getNickName(), Command.historyMsgCommand(clientHandler.getNickName(), sb.toString()));
        }
    }
}
