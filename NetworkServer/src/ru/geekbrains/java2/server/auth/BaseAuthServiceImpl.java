package ru.geekbrains.java2.server.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.java2.server.NetworkServer;
import ru.geekbrains.java2.server.dao.JdbcDao;


public class BaseAuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LogManager.getLogger(NetworkServer.class);

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
        LOGGER.info("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        LOGGER.info("Сервис аутентификации закрыт");
    }
}
