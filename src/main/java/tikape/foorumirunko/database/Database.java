package tikape.foorumirunko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAdd) throws ClassNotFoundException {
        this.databaseAddress = databaseAdd;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
    
    public void dropAllTables() {
        List<String> lauseet = dropTableLauseet();

        excecute(lauseet);
    }

    public void init() {
        List<String> lauseet = createTableLauseet();

        excecute(lauseet);
    }
    
    private void excecute(List<String> lauseet) {
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

    private List<String> createTableLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Kayttaja (kayttajan_id integer PRIMARY KEY, nimimerkki varchar(50));");
        lista.add("CREATE TABLE Alue (alueen_id integer PRIMARY KEY, alueen_nimi varchar(200), viestien_maara integer, viimeisinViesti timestamp);");
        lista.add("CREATE TABLE Viesti (kayttaja integer, alue integer, otsikko varchar(200), sisalto varchar(3000), aika timestamp, FOREIGN KEY(kayttaja) REFERENCES Kayttaja(kayttajan_id), FOREIGN KEY(alue) REFERENCES Alue(alueen_id));");
        
        return lista;
    }
    
    private List<String> dropTableLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        lista.add("DROP TABLE Kayttaja");
        lista.add("DROP TABLE Alue");
        lista.add("DROP TABLE Viesti");
        
        return lista;
    }

}
