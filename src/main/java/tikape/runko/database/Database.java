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

        //lista.add("DELETE FROM Viesti WHERE id = '1'");
        //lista.add("DELETE FROM Viesti WHERE id = '2'");
        
        lista.add("INSERT INTO Viesti (id, kayttaja, paivamaara, teksti, ketju) VALUES ('1', 'testikayttaja', '2017-01-01 00:00:00.000', 'tassa on tekstia', '1');");
        lista.add("INSERT INTO Viesti (id, kayttaja, paivamaara, teksti, ketju) VALUES ('2', 'testikayttajakaksi', '2017-01-02 00:00:00.000', 'tassa on enemman tekstia', '2');");
        
        lista.add("CREATE TABLE Opiskelija (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Platon');");
        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Aristoteles');");
        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Homeros');");

        return lista;
    }
}
