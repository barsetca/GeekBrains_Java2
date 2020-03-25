package ru.geekbrains.java2.client.command;

import java.io.Serializable;

public class ErrorCommand implements Serializable {
    private final String errorMsg;

    public ErrorCommand(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}