package org.example.site.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
    private Connection connection;
    public Connection getConnection()
    {
        return connection;
    }

    public void connect(String path) {
        try {
            connection= DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connected\n");
    }

    public void disconnect() {
        try {
            if(connection != null && !connection.isClosed() ) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected\n");
    }
}