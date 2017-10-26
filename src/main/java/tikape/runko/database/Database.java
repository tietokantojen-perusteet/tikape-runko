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
            "CREATE TABLE IF NOT EXISTS Smoothie (id integer PRIMARY KEY, nimi varchar(20), ohje varchar(1000));",
            "CREATE TABLE IF NOT EXISTS RaakaAine (id integer PRIMARY KEY, nimi varchar(20));",
            "CREATE TABLE IF NOT EXISTS SmoothieRaakaAine (smoothie_id integer, raaka_aine_id integer, jarjestys integer, maara varchar(10), FOREIGN KEY(smoothie_id) REFERENCES Smoothie(id), FOREIGN KEY(raaka_aine_id) REFERENCES RaakaAine(id));"
            // "CREATE TABLE IF NOT EXISTS ..."
        };
        return statements;
    }
}
