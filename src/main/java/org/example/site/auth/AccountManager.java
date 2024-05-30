package org.example.site.auth;

import org.example.site.database.DatabaseConnection;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager
{
    DatabaseConnection dbConnection;

    public AccountManager(DatabaseConnection dbconnection) {
        this.dbConnection = dbconnection;
    }

    public void init() {
        try {
            String createSQLTable = "CREATE TABLE IF NOT EXISTS accounts( " +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL)";


            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(createSQLTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void register(String login, String password){
        String hashedPassword =  BCrypt.withDefaults().hashToString(12, password.toCharArray());
        String insertSQL = "INSERT INTO accounts (username, password) VALUES (?, ?)";
        Connection connection = dbConnection.getConnection();
        try {
            Account account = getAccount(login);
            if(account != null){
                System.out.println("Account already exists");
                return;
            }
            PreparedStatement addUserStatement = connection.prepareStatement(insertSQL);
            addUserStatement.setString(1, login);
            addUserStatement.setString(2, hashedPassword);
            addUserStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean authenticate(String username, String password){
        String sql = "SELECT id, username, password FROM accounts WHERE username = ?";

        try {
            PreparedStatement statement = null;
            statement = dbConnection.getConnection().prepareStatement(sql);
            statement.setString(1, username);
            if (!statement.execute()) throw new RuntimeException("SELECT failed");

            ResultSet result = statement.getResultSet();

            if (!result.next())
            {
                System.out.println("No such user");
                return false;
            }

            String hashedPassword = result.getString(3);

            boolean okay = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword.toCharArray()).verified;
            if(okay)
            {
                System.out.println("Successfully authenticated");
                return true;
            }else
            {
                System.out.println("Failed to authenticate");
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Account getAccount(String username){
        String insertSQL = "SELECT id, username FROM accounts WHERE username = ?";
        Connection connection = dbConnection.getConnection();
        try{
            PreparedStatement usernameStatement = connection.prepareStatement(insertSQL);
            usernameStatement.setString(1, username);
            ResultSet results = usernameStatement.executeQuery();
            if(results.next()){
                int id = results.getInt("id");
                String usernameNew = results.getString("username");
                return new Account(usernameNew, id);
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
