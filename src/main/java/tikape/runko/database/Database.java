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
 
        
        
        lista.add("CREATE TABLE Alue (alue_id integer PRIMARY KEY NOT NULL, nimi varchar(40) NOT NULL);");
        lista.add("CREATE TABLE Keskustelu (keskustelu_id integer PRIMARY KEY NOT NULL, omaalue integer NOT NULL, otsikko varchar(100) NOT NULL,paivamaara date NOT NULL, aloittaja varchar(40) NOT NULL, aloitusviesti varchar(50000) NOT NULL, FOREIGN KEY(omaalue) REFERENCES Alue(alue_id));");
        lista.add("CREATE TABLE Viesti (viesti_id integer PRIMARY KEY NOT NULL, omakeskustelu integer NOT NULL, julkaisuaika date NOT NULL,kirjoittaja varchar(40) NOT NULL,teksti varchar(50000) NOT NULL, FOREIGN KEY(omakeskustelu) REFERENCES Keskustelu(keskustelu_id));" );


        return lista;
    }
}
