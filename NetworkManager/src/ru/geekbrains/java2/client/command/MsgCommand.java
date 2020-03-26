package ru.geekbrains.java2.client.command;

import java.io.Serializable;

public class MsgCommand implements Serializable {
    private final String username;
    private final String msg;

    public MsgCommand(String username, String msg) {
        this.username = username;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getUsername() {
        return username;
    }
}
