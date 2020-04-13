package ru.geekbrains.java2.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerApp {
    private static final int DEFAULT_PORT = 8189;

    private static final Logger LOGGER = LogManager.getLogger(ServerApp.class);

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
                String errorMsg = String.format("Неккоректный формат порта в args[0]: %s Будет использоваться " +
                        "порт по умолчанию %d.%n", args[0], DEFAULT_PORT);
                LOGGER.error(errorMsg, e);
            }
        }
        return port;
    }
}

