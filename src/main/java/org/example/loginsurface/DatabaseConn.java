package org.example.loginsurface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConn {

    static final String DB_URL = "jdbc:mysql://localhost:3307/Loginsurface";
    static final String USER = "root";
    static final String PASS = "Attila";
    // Open a connection
    public static void main(String[] args) {
        // Open a connection
       /* try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            // Execute a query
            System.out.println("Inserting records into the table...");
            String sql = "INSERT INTO myuser VALUES ('Zara', 456)";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
}

