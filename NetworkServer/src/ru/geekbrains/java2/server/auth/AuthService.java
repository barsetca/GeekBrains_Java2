package ru.geekbrains.java2.server.auth;

public interface AuthService {

    String getUserNameByLoginAndPassword(String login, String password);

    int updateUserNickName(String newNickName, String oldNickName);

    void start();

    void stop();

}
