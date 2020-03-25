package ru.geekbrains.java2.server;

import ru.geekbrains.java2.server.auth.AuthService;
import ru.geekbrains.java2.server.auth.BaseAuthServiceImpl;
import ru.geekbrains.java2.server.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkServer {

    private final int port;
    private final List<ClientHandler> clients = new ArrayList<>();
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

    public synchronized void broadCastMsg(String msg, ClientHandler owner) throws IOException {
        for (ClientHandler client : clients) {
            if (!client.equals(owner)) {
                client.sendMsg(msg);
            }
        }
    }

    public synchronized void oneDirect(String msg, String nickNameReceiver) throws IOException {

        for (ClientHandler client : clients) {
            if (client.getNickName().equals(nickNameReceiver)) {
                client.sendMsg(msg);
            }
        }
    }

    public synchronized void unSubscribe(ClientHandler client) {
        clients.remove(client);
    }

    public synchronized void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public List<ClientHandler> getClients() {
        return clients;
    }
}
