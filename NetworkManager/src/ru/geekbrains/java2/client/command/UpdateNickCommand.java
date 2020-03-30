package ru.geekbrains.java2.client.command;

import java.io.Serializable;

public class UpdateNickCommand implements Serializable {
    private String newNickName;
    private String oldNickName;

    public UpdateNickCommand(String newNickName, String oldNickName) {
        this.newNickName = newNickName;
        this.oldNickName = oldNickName;
    }

    public String getNewNickName() {
        return newNickName;
    }

    public String getOldNickName() {
        return oldNickName;
    }

}
