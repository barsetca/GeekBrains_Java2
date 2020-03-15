package lesson06.multiclient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerChatMultiClient {

    private static final Map<String, DataOutputStream> clientsNameOutMap = new HashMap<>();

    private static final List<String> msgChat = new ArrayList<>();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен, ожидаем подключения ...");
            System.out.println("Для разрыва всех подключений и выхода из прогпраммы программы введите exit");


            while (true) {
                Socket socket = serverSocket.accept();
// поток нового клиента
                new Thread(() -> {
                    String name = Thread.currentThread().getName();

                    // поток на прием и эхо
                    new Thread(() -> {
                        try {
                            System.out.println("Клиент: " + Thread.currentThread().getName() + " подключился");
                            DataInputStream in = new DataInputStream(socket.getInputStream());

                            while (true) {
                                try {
                                    String clientMsg = in.readUTF();
                                    String msg = Thread.currentThread().getName() + ": " + clientMsg;
                                    msgChat.add(msg);
                                    List<DataOutputStream> socketsOut = new ArrayList<>(clientsNameOutMap.values());
                                    for (DataOutputStream dataOutputStream : socketsOut) {
                                        dataOutputStream.writeUTF(msg);
                                    }
                                } catch (Exception e) {
                                    break;
                                }
                            }
                            clientsNameOutMap.remove(Thread.currentThread().getName());
                            socket.close();

                            System.out.println(Thread.currentThread().getName() + " вышел из чата exit");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }, name).start();
// поток на передачу сообщений от сервера
                    new Thread(() -> {
                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            clientsNameOutMap.put(Thread.currentThread().getName(), out);
                            for (String string : msgChat) {
                                out.writeUTF(string);
                            }

                            out.writeUTF("Дбро пожаловать в чат! Ваше имя в чате: " +
                                    Thread.currentThread().getName() + "\n" + "Для выхода из программы введите exit");
                            out.writeUTF("Кроме Вас в чате " + (clientsNameOutMap.size() - 1) + " человек");


                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }, name).start();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                        String serverMsg;
                        while ((serverMsg = reader.readLine()) != null) {
                            if (serverMsg.trim().isEmpty()) {
                                continue;
                            }
                            if (serverMsg.equalsIgnoreCase("exit")) {
                                serverSocket.close();
                                System.out.println("Сервер завершил работу. Для возобновления работы перезапустите сервер.");
                                System.exit(0);
                            } else {
                                System.out.println("Некорректная команда");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
