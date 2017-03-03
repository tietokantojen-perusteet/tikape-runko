package tikape.foorumirunko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
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
    
    public void instantiateTestData() {
        List<String> lauseet = insertKayttajaLauseet();
        lauseet.addAll(insertAlueLauseet());

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
        lista.add("CREATE TABLE Kayttaja (nimimerkki varchar(50) PRIMARY KEY);");
        lista.add("CREATE TABLE Alue (alueen_id integer PRIMARY KEY, alueen_nimi varchar(200), viestien_maara integer, viimeisin_viesti timestamp);");
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

    private List<String> insertKayttajaLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        lista.add("INSERT INTO Kayttaja VALUES ('Vilperi')");
        lista.add("INSERT INTO Kayttaja VALUES ('Matumbaman')");
        lista.add("INSERT INTO Kayttaja VALUES ('AxlMaxl')");
        lista.add("INSERT INTO Kayttaja VALUES ('Eemimeeminveieeviin')");
                
        return lista;
    }

    private List<String> insertAlueLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        lista.add("INSERT INTO Alue VALUES (1, 'Hauskat Kotivideo', 0, 0)");
        lista.add("INSERT INTO Alue VALUES (2, 'En minä, mutta ne muut!', 0, 0)");
        lista.add("INSERT INTO Alue VALUES (3, 'Saako täällä sienestää?', 0, 0)");
        lista.add("INSERT INTO Alue VALUES (666, 'Miksi Mikolla on pitkät sääret!1', 0, 0)");
                
        return lista;
    }

}
