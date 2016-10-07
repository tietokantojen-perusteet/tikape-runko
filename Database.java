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
        lista.add("CREATE TABLE Keskustelualue (alue_id integer PRIMARY KEY, nimi varchar(50) NOT NULL);");
        lista.add("CREATE TABLE Keskustelunavaus (avaus_id integer PRIMARY KEY, alue_id integer NOT NULL, nimi varchar(75) NOT NULL, FOREIGN KEY (alue_id) REFERENCES Keskustelualue (alue_id));");
        lista.add("CREATE TABLE Viesti (viesti_id integer, avaus_id integer NOT NULL, aika timestamp NOT NULL, sisalto varchar(1000) NOT NULL, nimimerkki varchar(30) NOT NULL, FOREIGN KEY (avaus_id) REFERENCES Keskustelunavaus (avaus_id));");
        lista.add("INSERT INTO Keskustelualue (nimi) VALUES ('Tietokoneet');");
        lista.add("INSERT INTO Keskustelualue (nimi) VALUES ('Musiikki');");
        lista.add("INSERT INTO Keskustelualue (nimi) VALUES ('Elokuvat');");

        return lista;
    }

//    public <T> List<T> queryAndCollect(String query, Collector<T> col, Object... params) throws SQLException {
//        if (debug) {
//            System.out.println("---");
//            System.out.println("Executing: " + query);
//            System.out.println("---");
//        }
//
//        List<T> rows = new ArrayList<>();
//        PreparedStatement stmt = this.getConnection().prepareStatement(query);
//        for (int i = 0; i < params.length; i++) {
//            stmt.setObject(i + 1, params[i]);
//        }
//
//        ResultSet rs = stmt.executeQuery();
//
//        while (rs.next()) {
//            if (debug) {
//                System.out.println("---");
//                System.out.println(query);
//                debug(rs);
//                System.out.println("---");
//            }
//
//            rows.add(col.collect(rs));
//        }
//
//        rs.close();
//        stmt.close();
//        return rows;
//    }
}
