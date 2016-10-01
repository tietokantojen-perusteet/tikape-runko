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

    /**
     * SQL-kyselyt, jotka suoritetaan palvelimen käynnistyessä
     * @return 
     */
    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        //Foreign keys päälle
        lista.add("PRAGMA foreign_keys = ON;");
        //Kategoriat -taulu
        lista.add("CREATE TABLE categories (categoryId integer PRIMARY KEY, title varchar(255));");
        //Alakategoriat -taulu
        lista.add("CREATE TABLE subCategories (subCatId integer PRIMARY KEY, catId integer , title varchar(255), description varchar(1024), FOREIGN KEY(catId) REFERENCES categories(categoryId));");
        //Viestiketjut
        lista.add("CREATE TABLE threads (threadId integer PRIMARY KEY, subCategoryId integer , userId integer, title varchar(255), creationDate varchar(255), FOREIGN KEY(subCategoryId) REFERENCES subCategories(subCatId), FOREIGN KEY(userId) REFERENCES users(userId));");
        //Viestiketjun postaukset
        lista.add("CREATE TABLE posts (postId integer PRIMARY KEY, threadId integer , userId integer , timestamp varchar(255), body varchar(4096), FOREIGN KEY(threadId) REFERENCES threads(threadId), FOREIGN KEY(userId) REFERENCES users(userId));");
        //Käyttäjät
        lista.add("CREATE TABLE users (userId integer PRIMARY KEY, username varchar(255), password varchar(255), salt varchar(255), userLevel integer);");
        
        //Kategoriat
//        lista.add("INSERT INTO categories (title) VALUES ('Testikategoria 1');");
//        lista.add("INSERT INTO categories (title) VALUES ('Testikategoria 2');");
//        lista.add("INSERT INTO subCategories (catId, title, description) VALUES (1,'Testialakategoria 1','Hello World');");
//        lista.add("INSERT INTO subCategories (catId, title, description) VALUES (1,'Testialakategoria 2','Hello World');");
//        lista.add("INSERT INTO subCategories (catId, title, description) VALUES (1,'Testialakategoria 3','Hello World');");
//        lista.add("INSERT INTO subCategories (catId, title, description) VALUES (2,'Testialakategoria 4','Hello World');");
//        lista.add("INSERT INTO subCategories (catId, title, description) VALUES (2,'Testialakategoria 5','Hello World');");
        //Alakategoriat
        //Jne..
        return lista;
    }
}
