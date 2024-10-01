package org.example;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

import static org.example.Main.HubWorld;

public class Database {

    /*
     database contruction
     */
    static String url = "localhost";
    static int port = 3306;
    static String database = "";
    static String username = "";
    static String LogU = "";
    static String LogP = "";
    static String password = "";
    static String email = "";
    static String Address = "";
    static String ZipCode = "";
    /*
    private varibles
     */
    private static Database db;
    private MysqlDataSource dataSource;
    private Database() {

    }
    private void initializeDataSource() {
        //try {

        dataSource = new MysqlDataSource();
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setURL("jdbc:mysql://" + url + ":" + port + "/" + database + "?serverTimezone=UTC");
        //} catch(SQLException ex){
        //jdbc:mysq
        //}
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void OrderProduct(Connection connection, Scanner scanner){
            if (email.isEmpty()){
                System.out.println("you must be logged in in to order products");
            }
    }
  public static void UserLogin(Connection connection, Scanner scanner){
        if (scanner.nextLine() == "2"){
            UserMaking(connection,scanner);
        } else
        try {
            System.out.println("Please enter username");
            LogU = scanner.nextLine();
            System.out.println("Please enter password");
            LogP = scanner.nextLine();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("Select username,Password,Email from UserData WHERE username ='"+ LogU + "'AND Password = '" +LogP+"'");
            while(result.next()){
                System.out.println("Welcome "+LogU);
              email = result.getString("Email");
            HubWorld(connection,scanner);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    static void UserReading(Connection connection, Scanner scanner){
        if (email.isEmpty()){
            System.out.println("sorry but to read users you must be logged in. if your not registerd type 2 else just press enter");
            Database.UserLogin(connection, scanner);
        }else
        System.out.println("SHOWING USERS:");
        try {String hashedPassword = hashPassword(password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("Select * from UserData WHERE Email = '"+email+"'");
            while(result.next()){
                System.out.println(result.getInt("id"));
                System.out.print("Name: ");
                System.out.println(result.getString("username"));
                System.out.print("Email: ");
                System.out.println(result.getString("email"));
                System.out.print("Password: ");
                System.out.println(hashedPassword);
                System.out.print("Address: ");
                System.out.println(result.getString("Address"));
                System.out.print("ZipCode: ");
                System.out.println(result.getString("ZipCode"));
            }


        } catch (SQLException ex) {
            System.out.println("the username and password");
            Database.PrintSQLException(ex);
        }
        HubWorld(connection, scanner);
    }
    static void UserRemoval(Connection connection, Scanner scanner){
        System.out.println("Please enter Email of your Account to Delete from DataBase");
        email = scanner.nextLine();
        try {
            String insertSQL = "DELETE FROM UserData where Email = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, email);
        } catch (SQLException ex) {
            System.out.println("the username and password");
            Database.PrintSQLException(ex);
        }
    }
    static void UserMaking(Connection connection, Scanner scanner){
        System.out.println("username:");
        username = scanner.nextLine();
        System.out.println("Email:");
        email = scanner.nextLine();
        System.out.println("Address"); // only put in your email up to before the @ like this but without the marks 'bently'@gmail.com so here you would only type in bently
        Address = scanner.nextLine();
        System.out.println("Zip Code");
        ZipCode = scanner.nextLine();
        try {
            Statement statement = connection.createStatement();
            String insertSQL = "INSERT INTO UserData (username,Email,Address,ZipCode) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, Address);
            preparedStatement.setString(4, ZipCode);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("the username and password");
            Database.PrintSQLException(ex);
        }

    }
    private Connection createConnection() {
        try {
            return dataSource.getConnection();
        }catch (SQLException ex){
            return null;
        }
    }
    public static  Connection getConnection() {
        if (db == null){
            db = new Database();
            db.initializeDataSource();
        }
        return db.createConnection();
    }
    public static void PrintSQLException(SQLException sqle) {
        PrintSQLException(sqle, false);
    }
    public static void PrintSQLException(SQLException sqle, Boolean printStackTrace) {
        while (sqle != null) {
            System.out.println("\n---SQLException Caught---\n");
            System.out.println("SQLState: " + sqle.getSQLState());
            System.out.println("ErrorCode: " + sqle.getErrorCode());
            System.out.println("Message: " + sqle.getMessage());
            if (printStackTrace) sqle.printStackTrace();
            sqle = sqle.getNextException();
        }
    }
}