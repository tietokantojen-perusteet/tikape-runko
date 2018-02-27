package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.sqlite.SQLiteConfig;

public class Database {

    private String databaseAddress;
    private SQLiteConfig sqliteconfig;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
        this.sqliteconfig = new SQLiteConfig();
        this.sqliteconfig.enforceForeignKeys(true);
    }

    public Connection getConnection() throws SQLException {
        //jos Herokun postgre on olemassa, niin käytetään sitä
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
            if (dbUrl != null && dbUrl.length() > 0) {
                return DriverManager.getConnection(dbUrl);
        }
        // Nyt sqlite-spesifinen. Käytännössä sama kuin laittaisi PRAGMA FOREIGN_KEYS = on
        return DriverManager.getConnection(databaseAddress, sqliteconfig.toProperties());
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
                + "raakaAineId integer, jarjestys integer,"
                + "maara varchar(255), ohje varchar(1000),"
                + "FOREIGN KEY (annosId) REFERENCES Annos(id) ON DELETE CASCADE,"
                + "FOREIGN KEY (raakaAineId) REFERENCES RaakaAine(id) ON DELETE CASCADE);");
        

        return lista;
    }
}
