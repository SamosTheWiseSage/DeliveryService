package org.example;

import java.sql.Connection;
import java.util.Scanner;

import static org.example.Database.email;


public class Main {
    static String input = "";
    public static void main(String[] args) {
        Database.username = "root";
        Database.password = "Underfell5958";
        //inte ändra port
        Database.database = "ITTest";
        Connection connection = Database.getConnection();
        Scanner scanner = new Scanner(System.in);
        HubWorld(connection, scanner);
    }
    public static void HubWorld(Connection connection, Scanner scanner){
        try{
            System.out.println("Welcome! this is the HubWorld where you can navigate to making Accounts,show accounts, and delete accounts");
            System.out.println("1: Register new User");
            System.out.println("2: Read all Users");
            System.out.println("3: Delete User");
            System.out.println("4: Login");
            input = scanner.nextLine();
            switch (input){
                case "1":
                    Database.UserMaking(connection,scanner);
                    break;
                case "2":
                    Database.UserReading(connection,scanner);
                    break;
                case "3":
                    Database.UserRemoval(connection,scanner);
                    break;
                case "4":
                    Database.UserLogin(connection,scanner);
                    break;
                case "5":
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}