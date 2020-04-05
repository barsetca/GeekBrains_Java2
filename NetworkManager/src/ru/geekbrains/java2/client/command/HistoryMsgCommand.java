package ru.geekbrains.java2.client.command;

import java.io.Serializable;

public class HistoryMsgCommand implements Serializable {
    private final String receiver;
    private final String msg;

    public HistoryMsgCommand(String receiver, String msg) {
        this.receiver = receiver;
        this.msg = msg;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMsg() {
        return msg;
    }
}
