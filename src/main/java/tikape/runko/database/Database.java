package tikape.runko.database;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database<T> {

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

    public void init() {
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

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Alue (id integer PRIMARY KEY, nimi varchar(100) NOT NULL);");

        lista.add("CREATE TABLE Keskustelu (id integer PRIMARY KEY, alue_id integer NOT NULL, otsikko varchar(100) NOT NULL, FOREIGN KEY(alue_id) REFERENCES Alue(id));");

        // Aika-sarake hankalasti, jotta getTimestamp-metodin haluamassa muodossa:
        lista.add("CREATE TABLE Viesti (id integer PRIMARY KEY, keskustelu_id integer NOT NULL, aika timestamp DEFAULT (strftime('%Y-%m-%d %H:%M:%f', 'now')), kayttaja varchar(30), sisalto varchar(10000) NOT NULL, FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id))");

        return lista;
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("CREATE TABLE Alue (id SERIAL PRIMARY KEY, nimi varchar(100) NOT NULL);");

        lista.add("CREATE TABLE Keskustelu (id SERIAL PRIMARY KEY, alue_id integer NOT NULL, otsikko varchar(100) NOT NULL, FOREIGN KEY(alue_id) REFERENCES Alue(id));");

        lista.add("CREATE TABLE Viesti (id SERIAL PRIMARY KEY, keskustelu_id integer NOT NULL, aika timestamp DEFAULT CURRENT_TIMESTAMP, kayttaja varchar(30), sisalto varchar(10000) NOT NULL, FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id))");

        return lista;
    }

    public List<T> queryAndCollect(String query, Collector<T> col, Object... params) throws SQLException {
        Connection connection = this.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        ResultSet rs = stmt.executeQuery();
        List<T> rows = new ArrayList<>();

        while (rs.next()) {
            rows.add(col.collect(rs));
        }

        rs.close();
        stmt.close();
        connection.close();

        return rows;
    }

    public void update(String updateQuery, Object... params) throws SQLException {
        Connection connection = this.getConnection();
        PreparedStatement stmt = connection.prepareStatement(updateQuery);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        stmt.execute();

        stmt.close();
        connection.close();
    }
}
