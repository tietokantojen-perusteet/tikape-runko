/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import tikape.runko.database.Database;

/**
 *
 * @author eetu
 */
public class TestausUI {
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Hello. Please give a name for the database file. " +
                "Include .db");
        String filename = sc.nextLine();
        
        Database database = new Database("jdbc:sqlite:" + filename);
        database.init();
        while(true) {
            System.out.println("Do you want to modify the database or test the forum?");
            System.out.println("mod\tmodify\ntest\ttest forum\nexit\tquit application");
            String modOrTest = sc.nextLine();
        
            if (modOrTest.equals("exit")) {
                break;
            } else if (!(modOrTest.equals("mod") || modOrTest.equals("test"))) {
                System.out.println("Unknown command.");
            } else if (modOrTest.equals("mod")) {
                enterModifyState(database, sc);
            } else if (modOrTest.equals("test")) {
                enterTestState(database, sc);
            }
        }
        System.out.println("Bye.");
    }
    
    private static void enterModifyState(Database db, Scanner sc) throws SQLException{
        System.out.println("q\tquery\nu\tupdate\nl\tlist all tables\n" +
                "t\tlist contents of a single table\nexit\texit program\n" +
                "help\tshow this list\n");
        while(true) {
            System.out.println("Enter desired command.");
            String mode = sc.nextLine();
            
            if (mode.equals("exit")) {
                System.out.println("Quitting database modification.");
                break;
            } else if (mode.equals("help")) {
                System.out.println("q\tquery\nu\tupdate\nl\tlist all tables\n" +
                    "t\tlist contents of a single table\nexit\texit modification\n" +
                    "help\tshow this list\n");
            } else if (mode.equals("l")) {
                Connection con = db.getConnection();
                
                ResultSet rs = con.createStatement().executeQuery(
                        "SELECT name FROM sqlite_master WHERE type='table'");
       
                while(rs.next()) {
                    System.out.println(rs.getString("name"));
                }
                System.out.println("");
                con.close();
                
            } else if (mode.equals("t")) {
                System.out.println("Enter name of table:");
                String table = sc.nextLine();
                Connection con = db.getConnection();
                
                
                try {
                    // PreparedStatement pst = con.prepareStatement("SELECT * FROM " + table);
                    // pst.setString(1, table);
                    // ResultSet rs = pst.executeQuery();
                    
                    ResultSet rs = con.createStatement().executeQuery(
                            "SELECT * FROM " + table);
                    
                    while(rs.next()) {
                        for (int i = 1;;i++) {
                            try {
                                String s = rs.getString(i);
                                System.out.print(s + "\t");
                            } catch (Throwable t){
                                System.out.println("");
                                break;
                            }
                        }
                    }                
                } catch (Throwable t) {
                    System.out.println("Couldn't list table contents.");
                }
                
                con.close();
            } else if (mode.equals("q")) {
                System.out.println("Enter statement:");
                String statement = sc.nextLine();
                
                Connection con = db.getConnection();
                ResultSet rs = con.createStatement().executeQuery(statement);
        
                while(rs.next()) {
                    for (int i = 1;;i++) {
                        try {
                            String s = rs.getString(i);
                            System.out.print(s + "\t");
                        } catch (Throwable t){
                            System.out.println("");
                            break;
                        }
                    }
                }
                con.close();
            } else if (mode.equals("u")) {
                System.out.println("Enter statement:");
                String statement = sc.nextLine();
                try {
                    db.update(statement);
                } catch (Throwable t) {}
            } else {
                System.out.println("Unknown command.");
                System.out.println("q\tquery / u\tupdate / l\tlist all tables / " +
                    "t\tlist contents of a single table / exit\texit program / " +
                    "help\tshow this list\n");
            }
        }
    }
    
    private static void enterTestState(Database db, Scanner sc) {
        System.out.println("This is a testing UI for the forum");
        return;
    }
    
}