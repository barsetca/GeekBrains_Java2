package ru.geekbrains.java2.server;

import ru.geekbrains.java2.client.Command;
import ru.geekbrains.java2.server.auth.AuthService;
import ru.geekbrains.java2.server.auth.BaseAuthServiceImpl;
import ru.geekbrains.java2.server.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class NetworkServer {

    private final int port;
    //private final List<ClientHandler> clients = new ArrayList<>();
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final AuthService authService;

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
}
