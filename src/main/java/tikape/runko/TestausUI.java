/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko;

import java.sql.Connection;
import java.sql.ResultSet;
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
        
        System.out.println("Hello. Please give a name for the database file.\n" +
                "Include .db");
        String filename = sc.nextLine();
        
        Database database = new Database("jdbc:sqlite:" + filename);
        database.init();
        
        while(true) {
            System.out.println("Enter 'q' for query, 'u' for update or 'exit' to quit.");
            String mode = sc.nextLine();
            
            if (mode.equals("exit")) {
                break;
            }
            
            System.out.println("Enter statement:");
            String statement = sc.nextLine();
            
            if (mode.equals("q")) {
                Connection con = database.getConnection();
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
                database.update(statement);
            } else {
                System.out.println("Did you enter 'q' or 'u'?");
            }
        }
        
        System.out.println("Bye.");
    }
}