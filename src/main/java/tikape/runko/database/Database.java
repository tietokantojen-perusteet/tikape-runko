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
        lista.add("CREATE TABLE Lauta\n"
                + "(\n"
                + "nimi varchar(20) PRIMARY KEY,\n"
                + "motd varchar(50)\n"
                + ");");
        lista.add("CREATE TABLE Lanka\n"
                + "(\n"
                + "id integer PRIMARY KEY,\n"
                + "otsikko varchar(50),\n"
                + "lauta varchar(20) NOT NULL,\n"
                + "FOREIGN KEY(lauta) REFERENCES Lauta(nimi)\n"
                + ")");
        lista.add("CREATE TABLE Viesti\n"
                + "(\n"
                + "id integer PRIMARY KEY,\n"
                + "sisalto varchar(1000) NOT NULL,\n"
                + "nimimerkki varchar(20),\n"
                + "aika timestamp,\n"
                + "lanka_id integer NOT NULL,\n"
                + "vastaus_id integer,\n"
                + "FOREIGN KEY(lanka_id) REFERENCES Lanka(id),\n"
                + "FOREIGN KEY(vastaus_id) REFERENCES Viesti(id)\n"
                + ");")
                
//        Model for db tables/inserts
//        lista.add("CREATE TABLE Opiskelija (id integer PRIMARY KEY, nimi varchar(255));");
//        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Platon');");
//        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Aristoteles');");
//        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Homeros');");

        return lista;
    }
}
