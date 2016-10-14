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
        lista.add("CREATE TABLE Alue ("
                + "alue_id integer PRIMARY KEY NOT NULL, "
                + "nimi varchar(40) NOT NULL);");
        lista.add("CREATE TABLE Keskustelu ("
                + "keskustelu_id integer PRIMARY KEY NOT NULL, "
                + "omaalue integer NOT NULL, "
                + "otsikko varchar(100) NOT NULL,"
                + "paivamaara \"timestamp\" TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S.000','now', 'localtime')), "
//                + "paivamaara timestamp date default (datetime('now','localtime')), "
                + "aloittaja varchar(40) NOT NULL, "
                + "aloitusviesti varchar(50000) NOT NULL, "
                + "FOREIGN KEY(omaalue) REFERENCES Alue(alue_id));");
        lista.add("CREATE TABLE Viesti ("
                + "viesti_id integer PRIMARY KEY NOT NULL, "
                + "omakeskustelu integer NOT NULL, "
//                + "julkaisuaika timestamp date default (datetime('now','localtime')),"
                + "julkaisuaika \"timestamp\" TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S.000','now', 'localtime')),"
                + "kirjoittaja varchar(40) NOT NULL,"
                + "teksti varchar(50000) NOT NULL, "
                + "FOREIGN KEY(omakeskustelu) REFERENCES Keskustelu(keskustelu_id));");
        lista.add("INSERT INTO Alue (alue_id, nimi) "
                + "VALUES (1, 'Kissat');");
        lista.add("INSERT INTO Alue (alue_id, nimi) "
                + "VALUES (2, 'Matematiikka');");
        lista.add("INSERT INTO Keskustelu (keskustelu_id, omaalue, otsikko, paivamaara, aloittaja, aloitusviesti)"
                + "VALUES (1,1,'Rodunjalostus on väärin', '2016-12-12 11:40:51.000','Seppo','Jep');");
        lista.add("INSERT INTO Keskustelu (keskustelu_id, omaalue, otsikko, paivamaara, aloittaja, aloitusviesti)"
                + "VALUES (2, 2, 'Algebra on meh', '2016-12-12 11:41:50.000', 'Ella', 'ei se oo kivaa');");
        lista.add("INSERT INTO Keskustelu (keskustelu_id, omaalue, otsikko, paivamaara, aloittaja, aloitusviesti)"
                + "VALUES (3, 2, 'Algebra on jee', '2016-12-12 11:42:51.000', 'Ella', 'jee kivaa');");
        lista.add("INSERT INTO Keskustelu (omaalue, otsikko, aloittaja, aloitusviesti)"
                + "VALUES (2, 'Lukuteoria on kivaa', 'Pyry', 'moi');");
        lista.add("INSERT INTO Viesti (viesti_id, omakeskustelu, julkaisuaika, kirjoittaja, teksti)"
                + "VALUES (1,1,'2016-12-12 11:44:51.01', 'Anna', 'Itse tykkään jalostaa kissoja');");
        lista.add("INSERT INTO Viesti (viesti_id, omakeskustelu, julkaisuaika, kirjoittaja, teksti)"
                + "VALUES (2, 2, '2016-12-12 11:45:51.01','Ella', 'oon eri mielt');");
        
        return lista;
    }
}
