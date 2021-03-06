package ru.geekbrains.java2.client;

import ru.geekbrains.java2.client.command.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {
    private CommandType type;
    private Object data;

    public Object getData() {
        return data;
    }

    public CommandType getType() {
        return type;
    }

    public static Command authCommand(String login, String password) {
        Command command = new Command();
        command.type = CommandType.AUTH;
        command.data = new AuthCommand(login, password);
        return command;
    }

    public static Command authErrorCommand(String errorMessage) {
        Command command = new Command();
        command.type = CommandType.AUTH_ERROR;
        command.data = new ErrorCommand(errorMessage);
        return command;
    }

    public static Command errorCommand(String errorMessage) {
        Command command = new Command();
        command.type = CommandType.ERROR;
        command.data = new ErrorCommand(errorMessage);
        return command;
    }

    public static Command msgCommand(String username, String message) {
        Command command = new Command();
        command.type = CommandType.MESSAGE;
        command.data = new MsgCommand(username, message);
        return command;
    }

    public static Command broadcastMsgCommand(String message) {
        Command command = new Command();
        command.type = CommandType.BROADCAST_MESSAGE;
        command.data = new BroadcastMsgCommand(message);
        return command;
    }

    public static Command privateMsgCommand(String receiver, String message) {
        Command command = new Command();
        command.type = CommandType.PRIVATE_MESSAGE;
        command.data = new PrivateMsgCommand(receiver, message);
        return command;
    }

    public static Command updateUsersListCommand(List<String> users) {
        Command command = new Command();
        command.type = CommandType.UPDATE_USERS_LIST;
        command.data = new UpdateUsersListCommand(users);
        return command;
    }

    public static Command endCommand() {
        Command command = new Command();
        command.type = CommandType.END;
        return command;
    }

    public static Command updateNickCommand(String newNickName, String oldNickName) {
        Command command = new Command();
        command.type = CommandType.UPDATE_NICK_NAME;
        command.data = new UpdateNickCommand(newNickName, oldNickName);
        return command;
    }

    public static Command historyMsgCommand(String receiver, String message) {
        Command command = new Command();
        command.type = CommandType.HISTORY_LINES;
        command.data = new HistoryMsgCommand(receiver, message);
        return command;
    }
}
