package tikape.runko.database;

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
    private Connection connection;
    
    public Database(String databaseAddress) throws ClassNotFoundException, SQLException {
        this.databaseAddress = databaseAddress;
        this.connection = getConnection();
        init();
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

    public void update(String sql) throws SQLException {
        connection.setAutoCommit(false);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        connection.commit();
    }

    
    private List<String> sqliteLauseet() {        

        List<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Kayttaja ("
                + "Id Integer NOT NULL PRIMARY KEY,"
                + "tunnus varchar(15) NOT NULL UNIQUE,"
                + "salasana varchar(15) NOT NULL,"
                + "email varchar(50), "
                + "onko_super Integer"
                + ");");
        lista.add("CREATE TABLE Keskustelu ("
                + "KeskusteluID Integer NOT NULL PRIMARY KEY,"
                + "Otsikko varchar(200) NOT NULL,"
                + "Aihealue varchar(200)"
                + ");");
        lista.add("CREATE TABLE Viesti ("
                + "KayttajanID Integer NOT NULL,"
                + "Kayttaja varchar(15) NOT NULL,"
                + "Keskustelu varchar(200) NOT NULL,"
                + "paivamaara DATE NOT NULL, "
                + "kellonaika DATETIME NOT NULL, "   
                + "sisalto varchar(200) NOT NULL,"
                + "FOREIGN KEY(kayttajanID) REFERENCES Kayttaja(Id),"
                + "FOREIGN KEY(keskustelu) REFERENCES Keskustelu(otsikko)"
                + ");");


        return lista;
    }
}
