package ru.geekbrains.java2.server;

public class ServerApp {
    private static final int DEFAULT_PORT = 8189;

    public static void main(String[] args) {
        int port = getServerPort(args);
        new NetworkServer(port).start();
    }

    private static int getServerPort(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.printf("Неккоректный формат порта: %s Будет использоваться порт по умолчанию.%n", args[0]);
                e.printStackTrace();
            }
        }
        return port;
    }
}

