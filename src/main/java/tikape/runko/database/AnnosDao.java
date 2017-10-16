package tikape.runko.database;

import tikape.runko.domain.Annos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnosDao implements Dao<Annos, Integer> {
    
      private Database database;

    public AnnosDao(Database database) {
        this.database = database;
    }

    @Override
    public Annos findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Annos a = new Annos(rs.getInt("id"), rs.getString("nimi"));

        stmt.close();
        rs.close();

        conn.close();

        return a;
    }
    
    // hakee kaikki asiakkaat tietokannasta ja palauttaa ne listalla
    @Override
    public List<Annos> findAll() throws SQLException {
        List<Annos> tulos = new ArrayList<>();
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos");

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            Annos a = new Annos(rs.getInt("id"), rs.getString("nimi"));

            tulos.add(a);
        }

        stmt.close();
        rs.close();

        conn.close();
        
        return tulos;
    }

    @Override
    public Annos saveOrUpdate(Annos object) throws SQLException {
        // jos asiakkaalla ei ole pääavainta, oletetaan, että asiakasta
        // ei ole vielä tallennettu tietokantaan ja tallennetaan asiakas
        
        if (object.getNimi() == null || object.getNimi().length() <= 0) {
            throw new java.lang.RuntimeException("Ei voi luoda annosta jolla ei ole nimeä!");
        }
        
        if (object.getId() == null) {
            return save(object);
        } else {
            return update(object);
        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Annos WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    private Annos save(Annos annos) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos"
                + " (nimi)"
                + " VALUES (?)");
        stmt.setString(1, annos.getNimi());

        stmt.executeUpdate();
          
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                Annos a = new Annos(rs.getInt(1), annos.getNimi());
                stmt.close(); 
                conn.close();
                return a;
            }
            else {
                throw new SQLException("Ei saatu avainta!");
            }
        }

    }

    private Annos update(Annos annos) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Annos SET"
                + " nimi = ?WHERE id = ?");
        stmt.setString(1, annos.getNimi());
        stmt.setInt(2, annos.getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return annos;
    }
    
}