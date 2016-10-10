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
        lista.add("CREATE TABLE Alue (id integer PRIMARY KEY, nimi varchar(100) NOT NULL)");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Platon');");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Aristoteles');");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Homeros');");
        
        
        lista.add("CREATE TABLE Keskustelu (id integer PRIMARY KEY, alue_id integer NOT NULL, otsikko varchar(100) NOT NULL, FOREIGN KEY(alue_id) REFERENCES Alue(id))");
        
        lista.add("CREATE TABLE Viesti (id integer PRIMARY KEY, keskustelu_id integer NOT NULL, aika timestamp DEFAULT CURRENT_TIMESTAMP, kayttaja varchar(30), "
                + "sisalto varchar(10000) NOT NULL, FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id)");

        return lista;
    }
}
