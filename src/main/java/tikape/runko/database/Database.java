package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

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
        // Lauseet tietokantataulujen luomiseen
        lista.add("CREATE TABLE Keskustelualue (" +
                    "id integer PRIMARY KEY, " +
                    "aihe varchar(50) NOT NULL UNIQUE);");
        lista.add("CREATE TABLE Keskustelunavaus (" +
                    "id integer PRIMARY KEY, " + 
                    "alue integer NOT NULL, " +
                    "aika timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                    "otsikko varchar(200) NOT NULL UNIQUE, " +
                    "FOREIGN KEY(alue) REFERENCES Keskustelualue(id));");
        lista.add("CREATE TABLE Viesti (" +
                    "id integer PRIMARY KEY, " +
                    "alue integer NOT NULL, " +
                    "avaus integer NOT NULL, " + 
                    "aika timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                    "nimimerkki varchar(20) NOT NULL, " + 
                    "sisalto varchar(1000) NOT NULL, " +
                    "FOREIGN KEY(avaus) REFERENCES Keskustelunavaus(id), " +
                    "FOREIGN KEY(alue) REFERENCES Keskustelualue(id));");
        return lista;
    }
    
    public void update(String query, Object... parameters) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement st = conn.prepareStatement(query);
            
        for (int i = 0; i < parameters.length; i++) {
            st.setObject(i + 1, parameters[i]);
        }
        
        st.execute();
        conn.close();
    }
}
