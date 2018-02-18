package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tikape.runko.Annos;
import tikape.runko.RaakaAine;

public class AnnosDao implements Dao<Annos, Integer>{
    private Database database;
    
    public AnnosDao(Database database){
        this.database = database;
    }
    
    public Annos findOne(Integer key) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Annos a = new Annos(rs.getString("nimi"), rs.getString("ohje"));
        
        stmt.close();
        rs.close();

        conn.close();

        return a;
    }
    
    public List<Annos> findAll() throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos");
        
        List<Annos> annokset = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Annos a = new Annos(rs.getString("nimi"), rs.getString("ohje"));
            annokset.add(a);
        }
        rs.close();
        stmt.close();
        conn.close();
        
        return annokset;
    }
    
    public Annos saveOrUpdate(Annos object) throws SQLException{
        if (object.id == null) {
            return save(object);
        } else {
            // muulloin päivitetään asiakas
            return update(object);
        }
    }
    
    public void delete(Integer key) throws SQLException{
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
                + " (nimi, ohje)"
                + " VALUES (?, ?)");
        stmt.setString(1, annos.getNimi());
        stmt.setString(2, annos.getOhje());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Annos"
                + " WHERE nimi = ? AND ohje = ?");
        stmt.setString(1, annos.getNimi());
        stmt.setString(2, annos.getOhje());
        
        ResultSet rs = stmt.executeQuery();
        
        rs.next(); // vain 1 tulos

        Annos a = new Annos(rs.getString("nimi"), rs.getString("ohje"));

        stmt.close();
        rs.close();
        conn.close();

        return a;
    }
    
    private Annos update(Annos annos) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Annos SET"
                + " nimi = ?, ohje = ? WHERE id = ?");
        stmt.setString(1, annos.getNimi());
        stmt.setString(2, annos.getOhje());
        stmt.setInt(2, annos.getId());
        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return annos;
    }
    
}
