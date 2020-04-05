package ru.geekbrains.java2.client;

public enum CommandType {
    AUTH,
    AUTH_ERROR,
    PRIVATE_MESSAGE,
    BROADCAST_MESSAGE,
    HISTORY_LINES,
    MESSAGE,
    UPDATE_USERS_LIST,
    UPDATE_NICK_NAME,
    ERROR,
    END
}
