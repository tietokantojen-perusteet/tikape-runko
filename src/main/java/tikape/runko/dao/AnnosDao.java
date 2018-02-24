
package tikape.runko.dao;
import java.sql.*;
import java.util.*;
import tikape.runko.database.Database;
import tikape.runko.domain.*;

public class AnnosDao implements Dao<Annos, Integer> {
    private Database database;
    
    public AnnosDao(Database database){
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
    
    @Override
    public List<Annos> findAll() throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT * FROM Annos;");
        ResultSet rs = stmt.executeQuery();
        List<Annos> annokset = new ArrayList<>();
        
        while (rs.next()){
            Annos a = new Annos(rs.getInt("id"), rs.getString("nimi"));
            annokset.add(a);
        }
        
        stmt.close();
        rs.close();
        conn.close();
        
        return annokset;
    }
    
    @Override
    public void delete(Integer key) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Annos WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    
    @Override
    public Annos saveOrUpdate(Annos annos) throws SQLException{
        if (findOne(annos.getId()) == null){
            return save(annos);
        } else {
            return update(annos);
        }
    }
    
    public Annos save(Annos annos) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos "
                + "(id, nimi"
                + "VALUES (?, ?");
        stmt.setInt(1, annos.getId());
        stmt.setString(2, annos.getNimi());
        stmt.executeUpdate();
        stmt.close();
        
        stmt = conn.prepareStatement("SELECT * FROM Annos"
                                + " WHERE id = ? AND nimi = ?");
        stmt.setInt(1, annos.getId());
        stmt.setString(2, annos.getNimi());

        ResultSet rs = stmt.executeQuery();
        rs.next();

        Annos a = new Annos(rs.getInt("id"), rs.getString("nimi"));
        stmt.close();
        rs.close();
        conn.close();
        
        return a;
    }
    
    public Annos update(Annos annos) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Annos SET"
                + "nimi = ? WHERE id = ?");
        stmt.setString(1, annos.getNimi());
        stmt.setInt(2, annos.getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return annos;
    }
    
    
}
