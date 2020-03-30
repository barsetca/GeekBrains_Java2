package ru.geekbrains.java2.server.dao;

import java.sql.*;

public class JdbcDao {

    private static Connection CONNECTION;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            CONNECTION = DriverManager.getConnection("jdbc:sqlite:chatusers.s3db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public /*static*/ String getUserNameByLoginAndPassword(String login, String password) {
        try (PreparedStatement statement = CONNECTION.prepareStatement("SELECT nick_name FROM users WHERE login=? AND password=?")) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next() ? null : resultSet.getString("nick_name");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public /*static*/ int updateUserNickName(String newNickName, String oldNickName) {

        try (PreparedStatement preparedStatement =
                     CONNECTION.prepareStatement("UPDATE users SET nick_name = ? WHERE nick_name = ?")) {
            preparedStatement.setString(1, newNickName);
            preparedStatement.setString(2, oldNickName);
            return (preparedStatement.executeUpdate());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void CloseConnectionDB() {
        try {
            CONNECTION.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Соединения закрыты");
    }
}
