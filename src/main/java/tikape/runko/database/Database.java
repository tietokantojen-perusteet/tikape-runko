package tikape.runko.database;

import java.sql.*;

public class Database {
    private final String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
    
    public void createTables() {
        String[] statements = this.createTableStatements();
        
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            
            for (String s : statements) {
                statement.execute(s);
            }
            
            statement.close();
            connection.close();
            
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    
    private String[] createTableStatements() {
        String[] statements = {
            // "CREATE TABLE IF NOT EXISTS Ingredient (id integer PRIMARY KEY, name text)",
            // "CREATE TABLE IF NOT EXISTS ..."
        };
        return statements;
    }
}
