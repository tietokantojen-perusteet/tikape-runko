package tikape.runko.database;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public String getDatabaseAddress() {
        return databaseAddress;
    }
    

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet =sqliteLauseet();
        }

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

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("DROP TABLE Alue; ");
        lista.add("DROP TABLE Aihe; ");
        lista.add("DROP TABLE Viesti; ");        
        lista.add("CREATE TABLE Alue (alue_id SERIAL PRIMARY KEY, "
                + "kuvaus varchar(50) NOT NULL)"
                + "UNIQUE(kuvaus); ");
        lista.add("INSERT INTO Alue (kuvaus) VALUES ('Postgres');");
        lista.add("CREATE TABLE Aihe (aihe_id SERIAL PRIMARY KEY, "
                + "otsikko varchar(50) NOT NULL, "
                + "alue_id integer NOT NULL REFERENCES Alue(alue_id)); ");        
        lista.add("INSERT INTO Aihe (otsikko, alue_id) VALUES ('Miten Postgres toimii', 1);");
        lista.add("CREATE TABLE Viesti (viesti_id SERIAL PRIMARY KEY, "
                + "aihe_id integer NOT NULL REFERENCES Aihe(aihe_id), "
                + "teksti varchar(500) NOT NULL, "
                + "nimimerkki varchar(25) NOT NULL, "
                + "ajankohta timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP); ");         
        lista.add("INSERT INTO Viesti (aihe_id, teksti, nimimerkki) VALUES (1, 'Postgre on hankala alkuuun', 'Jyrki');");
      

        return lista;
    }
    
    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Alue (alue_id integer PRIMARY KEY, "
                + "kuvaus varchar(50) NOT NULL UNIQUE); ");
        lista.add("INSERT INTO Alue (kuvaus) VALUES ('SQLite');");
        lista.add("CREATE TABLE Aihe (aihe_id integer PRIMARY KEY, "
                + "otsikko kuvaus varchar(50) NOT NULL, "
                + "alue_id integer NOT NULL, "
                + "FOREIGN KEY(alue_id) REFERENCES Alue(alue_id)); ");        
        lista.add("INSERT INTO Aihe (otsikko, alue_id) VALUES ('SQLite rules', 1);");
        lista.add("CREATE TABLE Viesti (viesti_id integer PRIMARY KEY, "
                + "aihe_id integer NOT NULL, "
                + "teksti varchar(500) NOT NULL, "
                + "nimimerkki varchar(25) NOT NULL, "
                + "ajankohta datetime NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(aihe_id) REFERENCES Aihe(aihe_id)); ");         
        lista.add("INSERT INTO Viesti (aihe_id, teksti, nimimerkki) VALUES (1, 'SQLite Studio on ihku', 'Jyrki');");

        return lista;
    }
}
