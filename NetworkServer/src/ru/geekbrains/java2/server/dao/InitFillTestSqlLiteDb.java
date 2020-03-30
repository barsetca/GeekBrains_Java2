package ru.geekbrains.java2.server.dao;

import java.sql.*;

public class InitFillTestSqlLiteDb {

    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;

    public static void main(String[] args) {
        try {
            setConnectionDb();
            dropAndCreateDb();
            fillDb();
            readDB();
            updateUser("nick1", "nick2");
            readDB();
            closeDB();

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection setConnectionDb() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:chatusers.s3db");

        System.out.println("База Подключена!");
        return connection;
    }

    public static void dropAndCreateDb() throws ClassNotFoundException, SQLException {
        statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS users;");
        statement.execute(
                "CREATE TABLE 'users' (" +
                        "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "'login' VARCHAR NOT NULL, " +
                        "'password' VARCHAR NOT NULL, " +
                        "'nick_name' VARCHAR NOT NULL UNIQUE);");

        statement.execute("CREATE UNIQUE INDEX users_unique_login_idx ON users (login)");

        System.out.println("Таблица создана или уже существует.");
    }

    public static void fillDb() throws SQLException {

        statement.execute("INSERT INTO 'users' ('login', 'password', 'nick_name') VALUES ('login1', 'password1', 'nick1'); ");
        statement.execute("INSERT INTO 'users' ('login', 'password', 'nick_name') VALUES ('login2', 'password2', 'nick2'); ");
        statement.execute("INSERT INTO 'users' ('login', 'password', 'nick_name') VALUES ('login3', 'password3', 'nick3'); ");

        System.out.println("Таблица заполнена");
    }

    public static void readDB() throws ClassNotFoundException, SQLException {
        resultSet = statement.executeQuery("SELECT * FROM users");

        while (resultSet.next()) {
            System.out.println("ID = " + resultSet.getInt("id"));
            System.out.println("login = " + resultSet.getString("login"));
            System.out.println("password = " + resultSet.getString("password"));
            System.out.println("nick_name = " + resultSet.getString("nick_name"));
            System.out.println();
        }
        System.out.println("Таблица выведена");
    }

    public static void updateUser(String newNickName, String oldNickName) {

        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE users SET nick_name = ? WHERE nick_name = ?");
            preparedStatement.setString(1, newNickName);
            preparedStatement.setString(2, oldNickName);
            System.out.println(preparedStatement.executeUpdate());
        } catch (SQLException e) {
            System.out.printf("Пользователь с nickName = %s уже существует%n", newNickName);
        }


    }

    public static void closeDB() throws ClassNotFoundException, SQLException {
        connection.close();
        statement.close();
        resultSet.close();

        System.out.println("Соединения закрыты");
    }
}
