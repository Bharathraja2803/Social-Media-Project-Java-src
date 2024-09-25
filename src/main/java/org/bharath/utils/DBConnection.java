package org.bharath.utils;

import org.bharath.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection = null;
    private static Connection connection_ = null;

    private DBConnection(){
        String username = "postgres";
        String password = "postgres";
        try {
            String url = "jdbc:postgresql://127.0.0.1:5432/SocialMediaDB";

            Class.forName("org.postgresql.Driver");

            connection_ = DriverManager.getConnection(
                    url, username, password);


        } catch (ClassNotFoundException | SQLException e) {
            Main.LOGGER.severe(e.toString());
        }
    }

    public static Connection getConnection(){
        if(dbConnection == null){
            dbConnection = new DBConnection();
        }
        return connection_;
    }

    @Override
    protected void finalize() throws Throwable {
        connection_.close();
        super.finalize();
    }
}
