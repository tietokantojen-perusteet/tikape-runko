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
        lista.add("CREATE TABLE Annos (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Lihamakaronilaatikko');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Munakas');");
        
        lista.add("CREATE TABLE RaakaAine (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Makaroni');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Maito');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Kananmuna');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Jauheliha');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Herkkusieni');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Vesi');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Paprika');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Juusto');");
        
        lista.add("CREATE TABLE AnnosRaakaAine (annosId integer,"
                + "raakaAineId integer, nimi varchar(255), jarjestys integer,"
                + "maara varchar(255), ohje varchar(1000),"
                + "FOREIGN KEY (annosId) REFERENCES Annos(id),"
                + "FOREIGN KEY (raakaAineId) REFERENCES RaakaAine(id));");
        

        return lista;
    }
}
