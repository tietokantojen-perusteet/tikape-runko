package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
//        lista.add("CREATE TABLE Alue (id integer PRIMARY KEY, nimi varchar(255));");
//        lista.add("CREATE TABLE Aihe (id integer PRIMARY KEY, alue_id integer, nimi varchar(255));");
//        lista.add("CREATE TABLE Viesti (id integer PRIMARY KEY, aihe_id integer, nimi varchar(255), text varchar(400), time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL);");
//        lista.add("INSERT INTO Alue (nimi) VALUES ('SQL');");
//        lista.add("INSERT INTO Alue (nimi) VALUES ('Java');");
//        lista.add("INSERT INTO Alue (nimi) VALUES ('Html');");
//        lista.add("INSERT INTO Aihe (alue_id, nimi) VALUES (1, 'joopajoo');");
//        lista.add("INSERT INTO Aihe (alue_id, nimi) VALUES (2, 'opajoo');");
//        lista.add("INSERT INTO Aihe (alue_id, nimi) VALUES (3, 'ajoo');");
//        lista.add("INSERT INTO Viesti (aihe_id, nimi, text) VALUES (2, 'joo', 'blaablaablaa');");
        
        return lista;
    }
}
