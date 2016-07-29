package aneere.runko.database;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;
    
    public Database(String databaseAddress) throws Exception {
        this.databaseAddress = databaseAddress;
        init();
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

    private void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
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

    public void update(String sql) throws SQLException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        connection.commit();
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        //lista.add("DROP TABLE Tuote;");
        // heroku käyttää SERIAL-avainsanaa uuden tunnuksen automaattiseen luomiseen
        //lista.add("INSERT INTO Viesti VALUES(1, 1, 1, '2016-06-23 23:09:11.946', 'Tosi magee!!')");
//        lista.add("DROP TABLE Kayttaja;");
//        lista.add("DROP TABLE Keskustelu");
//        lista.add("CREATE TABLE Kayttaja ("
//                + "ID SERIAL PRIMARY KEY,"
//                + "tunnus varchar(15) NOT NULL UNIQUE,"
//                + "salasana varchar(15),"
//                + "email varchar(50), "
//                + "onko_super Integer"
//                + ");");
//        lista.add("CREATE TABLE Keskustelu ("
//                + "KeskusteluID SERIAL PRIMARY KEY, "
//                + "Otsikko varchar(200) NOT NULL, "
//                + "Aihealue varchar(200) NOT NULL"
//                + ");");
//        lista.add("CREATE TABLE Viesti ("
//                + "ViestiID SERIAL PRIMARY KEY, "
//                + "Kayttaja Integer, "
//                + "Keskustelu Integer, "
//                + "kellonaika TIMESTAMP, "
//                + "sisalto varchar(500), "
//                + "FOREIGN KEY(Kayttaja) REFERENCES Kayttaja(ID), "
//                + "FOREIGN KEY(Keskustelu) REFERENCES Keskustelu(KeskusteluID)"
//                + ");");
//
//        lista.add("INSERT INTO Keskustelu VALUES (1,'Harley Davidson', 'Mopot');");
//        lista.add("INSERT INTO Keskustelu VALUES (2,'Fender Stratocaster', 'Kitarat');");
//        lista.add("INSERT INTO Keskustelu VALUES (3,'iPhone', 'Puhelimet');");
//        lista.add("INSERT INTO Keskustelu VALUES (4,'Kehitysehdotuksia', 'Yleinen');");

        return lista;
    }
    private List<String> sqliteLauseet() {        

        List<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Kayttaja ("
                + "ID Integer PRIMARY KEY,"
                + "tunnus varchar(15) NOT NULL UNIQUE,"
                + "salasana varchar(15),"
                + "email varchar(50), "
                + "onko_super Integer"
                + ");");
        lista.add("CREATE TABLE Keskustelu ("
                + "KeskusteluID Integer PRIMARY KEY,"
                + "Otsikko varchar(200) NOT NULL,"
                + "Aihealue varchar(200) NOT NULL"
                + ");");
        lista.add("CREATE TABLE Viesti ("
                + "ViestiID Integer PRIMARY KEY,"
                + "Kayttaja Integer,"
                + "Keskustelu Integer,"
                + "kellonaika TIMESTAMP,"   
                + "sisalto varchar(500),"
                + "FOREIGN KEY(Kayttaja) REFERENCES Kayttaja(ID),"
                + "FOREIGN KEY(Keskustelu) REFERENCES Keskustelu(KeskusteluID)"
                + ");");

        return lista;
    }
}
