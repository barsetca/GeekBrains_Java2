package ru.geekbrains.java2.server.auth;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthServiceImpl implements AuthService {

    private static final List<UserData> USER_DATA_LIST = new ArrayList<>();

    static {
        USER_DATA_LIST.add(new UserData("login1", "password1", "nick1"));
        USER_DATA_LIST.add(new UserData("login2", "password2", "nick2"));
        USER_DATA_LIST.add(new UserData("login3", "password3", "nick3"));
    }

    private static class UserData {
        private String login;
        private String password;
        private String userName;

        public UserData(String login, String password, String userName) {
            this.login = login;
            this.password = password;
            this.userName = userName;
        }
    }

    @Override
    public String getUserNameByLoginAndPassword(String login, String password) {
        for (UserData userData : USER_DATA_LIST) {
            if (userData.login.equals(login) && userData.password.equals(password)) {
                return userData.userName;
            }
        }
        return null;
    }

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации закрыт");
    }
}
