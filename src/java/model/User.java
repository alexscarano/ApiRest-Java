/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.*;
import java.util.ArrayList;
import web.AppListener;

public class User {
    private long rowId;
    private String name;
    private String login;
    private String role;
    private String passwordHash;
    
    public static String getCreateStatement(){
        return "CREATE TABLE IF NOT EXISTS users (\n"
               + "login VARCHAR(50) UNIQUE NOT NULL\n"
               + ", name VARCHAR(100) NOT NULL\n"
               + ", role VARCHAR(20) NOT NULL \n"
               + ", password_hash VARCHAR(255) NOT NULL\n"
               + ")";
    }
    
    public static ArrayList<User> getUsers() throws Exception{
        ArrayList<User> list = new ArrayList<>();
        Connection conn = AppListener.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * FROM users");
        while(rs.next()){
            long rowId = rs.getLong("rowId");
            String login = rs.getString("login");
            String name = rs.getString("name");
            String role = rs.getString("role");
            String passwordHash = rs.getString("password_hash");
            list.add(new User(rowId, login, name, role, passwordHash));
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        return list;
    }
    
    public static User getUser(String login, String password) throws Exception{
      User user = null;
      Connection conn = AppListener.getConnection();
      String sql = "SELECT rowid, * FROM users WHERE login=? AND password_hash=?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, login); // login=?
      stmt.setString(2, AppListener.getMd5Hash(password)); // password_hash ?
      ResultSet rs = stmt.executeQuery();
      if(rs.next()){ // If pois só retorna um registro, se retornasse varios seria um while
          long rowId = rs.getLong("rowid");
          String name = rs.getString("name");
          String role = rs.getString("role");
          String passwordHash = rs.getString("password_hash");
          user = new User(rowId, name, login, role, passwordHash);
      }
      rs.close();
      stmt.close();
      conn.close();
      return user;
    }
    
    public static void insertUser(String login, String name, String role, String password) throws Exception { 
        Connection conn = AppListener.getConnection();
        String sql = "INSERT INTO users(login, name, role, password_hash) " + "VALUES(?, ?, ? ,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt.setString(1, login);
        stmt.setString(2, name);
        stmt.setString(3, role);
        stmt.setString(4, AppListener.getMd5Hash(password));
        
        stmt.execute();
        stmt.close();
        conn.close();
    }
    
    public static void updateUser(String login, String name, String role, String password) throws Exception { 
        Connection conn = AppListener.getConnection();
        String sql = "UPDATE users SET name=?, role=?, password_hash=? WHERE login=?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, name);
        stmt.setString(2, role);
        stmt.setString(3, AppListener.getMd5Hash(password));
        stmt.setString(4, login);

        stmt.execute();
        stmt.close();
        conn.close();
    }
    
    public static void deleteUser(long rowId) throws Exception { 
        Connection conn = AppListener.getConnection();
        String sql = "DELETE FROM users WHERE rowId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setLong(1, rowId);
        stmt.execute();
        stmt.close();
        conn.close();
    }
    
    
    public User(long rowId, String name, String login, String role, String passwordHash) {
        this.rowId = rowId;
        this.name = name;
        this.login = login;
        this.role = role;
        this.passwordHash = passwordHash;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
}
