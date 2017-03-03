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
import java.util.List;
import java.util.Scanner;
import tikape.runko.database.Database;
import tikape.runko.database.KeskustelualueDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Keskustelualue;

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
        KeskustelualueDao alueDao = new KeskustelualueDao(database);
        ViestiDao viestiDao = new ViestiDao(database);
        
        
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
                enterTestState(database, sc, alueDao, viestiDao);
            }
        }
        System.out.println("Bye.");
    }
    
    private static void enterModifyState(Database db, Scanner sc) throws SQLException {
        System.out.println("Entering database modification.\n");
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
                System.out.println("q\tquery\nu\tupdate\nl\tlist all tables\n" +
                    "t\tlist contents of a single table\nexit\texit program\n" +
                    "help\tshow this list\n");
            }
        }
    }
    
    private static void enterTestState(Database db, Scanner sc,
            KeskustelualueDao ad, ViestiDao vd) throws SQLException {
        System.out.println("This is a testing UI for the forum");
        List<String> topics = printAndGetTopics(ad);
        while(true) {
            System.out.println("Submit topic to enter or 'exit' to leave.");
            String command = sc.nextLine();
            
            if (command.equals("exit")) {
                break;
            } else if (topics.contains(command)) {
                enterTopic(ad.getIdByTopic(command));
            } else {
                System.out.println("Unknown command.");
            }
        }
        return;
    }
    
    private static List<String> printAndGetTopics(KeskustelualueDao ad) throws SQLException {
        List<String> names = new ArrayList<>();
        for (String[] info : ad.lukumaaratPerKA()) {
            System.out.format("%s\tthreads: %s\tmessages: %s\t%s\n",
                    info[0], info[1], info[2], info[3]);
            names.add(info[0]);
        }
        return names;
    }
    
    private static void enterTopic(String topic) {
        // todo
    }
    
    private static List<String> printAndGetThreads() {
        return null;
    }
    
}