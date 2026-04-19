package org.example.loginsurface;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public void saveUser(String name, String mail, String phone, String password) throws Exception {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO myuser(name, mail, phone, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseConn.DB_URL, DatabaseConn.USER, DatabaseConn.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, mail);
            pstmt.setString(3, phone);
            pstmt.setString(4, hashedPassword);
            pstmt.executeUpdate();
        }
    }

    public void updateUser(String originalName, String name, String mail, String phone, String password) throws Exception {
        String sql;
        if (password == null || password.isEmpty()) {
            sql = "UPDATE myuser SET name=?, mail=?, phone=? WHERE name=?";
        } else {
            sql = "UPDATE myuser SET name=?, mail=?, phone=?, password=? WHERE name=?";
        }
        try (Connection conn = DriverManager.getConnection(DatabaseConn.DB_URL, DatabaseConn.USER, DatabaseConn.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, mail);
            pstmt.setString(3, phone);
            if (password == null || password.isEmpty()) {
                pstmt.setString(4, originalName);
            } else {
                pstmt.setString(4, BCrypt.hashpw(password, BCrypt.gensalt()));
                pstmt.setString(5, originalName);
            }
            pstmt.executeUpdate();
        }
    }

    public void deleteUser(String name) throws Exception {
        String sql = "DELETE FROM myuser WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConn.DB_URL, DatabaseConn.USER, DatabaseConn.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    public List<User> loadUsers() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT name, mail, phone, password FROM myuser";
        try (Connection conn = DriverManager.getConnection(DatabaseConn.DB_URL, DatabaseConn.USER, DatabaseConn.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("name"),
                        rs.getString("mail"),
                        rs.getString("phone"),
                        rs.getString("password")
                ));
            }
        }
        return users;
    }
}