
package tikape.foorumirunko.database;

import java.sql.*;
import java.util.*;
import tikape.foorumirunko.domain.Kayttaja;
/**
 *
 * @author eemitant
 * @author xvixvi
 */
public class KayttajaDao implements Dao<Kayttaja, String> {

    private Database database;
    
    public KayttajaDao(Database d) {
        database = d;
    }
    
    @Override
    public Kayttaja findOne(String key) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kayttaja WHERE kayttajan_id = ?");
        stmt.setObject(1, key);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String nimimerkki = rs.getString("nimimerkki");

        Kayttaja k = new Kayttaja(nimimerkki);

        rs.close();
        stmt.close();
        con.close();

        return k;
    }

    @Override
    public List findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja");

        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat = new ArrayList<>();
        while (rs.next()) {
            String nimimerkki = rs.getString("nimimerkki");

            kayttajat.add(new Kayttaja(nimimerkki));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kayttajat;
    }

    @Override
    public void delete(String key) throws SQLException {
        
    }
    
}
