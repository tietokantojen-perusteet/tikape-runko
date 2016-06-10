package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.domain.Kayttaja;
import tikape.runko.database.Database;

public class KayttajaDao implements Dao<Kayttaja, Integer> {
    
    private Database database;
    
    public KayttajaDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Kayttaja findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String tunnus = rs.getString("tunnus");
        String salasana = rs.getString("salasana");
        String email = rs.getString("email");

        Kayttaja k = new Kayttaja(id, tunnus, salasana, email);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }
    
    @Override
    public List<Kayttaja> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja");

        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("tunnus");
            String salasana = rs.getString("salasana");
            String email = rs.getString("email");

            kayttajat.add(new Kayttaja(id, nimi, salasana, email));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kayttajat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
    public void luoKayttaja(Kayttaja kayttaja) throws SQLException {
        String sql = "INSERT INTO Kayttaja "
                + "(Id, tunnus, salasana, email) VALUES ("
                + (kayttaja.getId()) + ", "
                + s(kayttaja.getTunnus()) + ", "
                + s(kayttaja.getSalasana()) + ", "
                + s(kayttaja.getEmail()) + " );";
        
        database.update(sql);
        
    }
        
    private String s(String s) {
        return "'" + s + "'";
    }

}
