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
        //Kategoriat -taulu
        lista.add("CREATE TABLE categories (categoryId integer PRIMARY KEY, title varchar(255));");
        //Alakategoriat -taulu
        lista.add("CREATE TABLE subCategories (subCatId integer PRIMARY KEY, catId integer , title varchar(255), description varchar(1024), FOREIGN KEY catId REFERENCES categories(categoryId));");
        //Viestiketjut
        lista.add("CREATE TABLE threads (threadId integer PRIMARY KEY, subCategoryId integer , title varchar(255), creationDate varchar(255), FOREIGN KEY subCategoryId REFERENCES subCategories(subCatId));");
        //Viestiketjun postaukset
        lista.add("CREATE TABLE posts (postId integer PRIMARY KEY, threadId integer , userId integer FOREIGN KEY users(userId), timestamp varchar(255), body varchar(40960, FOREIGN KEY threadId REFERENCES threads(threadId));");
        //Käyttäjät
        lista.add("CREATE TABLE users (userId integer PRIMARY KEY, username varchar(255), password varchar(255), salt varchar(255), userLevel integer);");
        return lista;
    }
}
