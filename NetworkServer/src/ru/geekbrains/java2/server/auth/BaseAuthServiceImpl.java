package ru.geekbrains.java2.server.auth;

import ru.geekbrains.java2.server.dao.JdbcDao;


public class BaseAuthServiceImpl implements AuthService {

    JdbcDao jdbcDao = new JdbcDao();

    @Override
    public String getUserNameByLoginAndPassword(String login, String password) {
        return jdbcDao.getUserNameByLoginAndPassword(login, password);
    }

    @Override
    public int updateUserNickName(String newNickName, String oldNickName) {
        return jdbcDao.updateUserNickName(newNickName, oldNickName);
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
