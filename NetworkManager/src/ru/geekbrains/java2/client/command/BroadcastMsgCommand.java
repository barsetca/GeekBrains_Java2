package ru.geekbrains.java2.client.command;

import java.io.Serializable;

public class BroadcastMsgCommand implements Serializable {

    private final String msg;

    public BroadcastMsgCommand(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
