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
        lista.add("CREATE TABLE Alue (alue_id integer PRIMARY KEY, "
                + "kuvaus varchar(50) NOT NULL UNIQUE); ");
        lista.add("INSERT INTO Alue (kuvaus) VALUES ('Ohjelmointi');");
        lista.add("CREATE TABLE Aihe (aihe_id integer PRIMARY KEY, "
                + "otsikko kuvaus varchar(50) NOT NULL, "
                + "alue_id integer NOT NULL, "
                + "FOREIGN KEY(alue_id) REFERENCES Alue(alue_id)); ");        
        lista.add("INSERT INTO Aihe (otsikko, alue_id) VALUES ('Java on jees', 1);");
        lista.add("CREATE TABLE VIESTI (viesti_id integer PRIMARY KEY, "
                + "aihe_id integer NOT NULL, "
                + "teksti varchar(500) NOT NULL, "
                + "nimimerkki varchar(25) NOT NULL, "
                + "ajankohta datetime NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(aihe_id) REFERENCES Aihe(aihe_id)); ");         
        lista.add("INSERT INTO Viesti (aihe_id, teksti, nimimerkki) VALUES (1, 'Java on ihku', 'Jyrki');");

        return lista;
    }
}
