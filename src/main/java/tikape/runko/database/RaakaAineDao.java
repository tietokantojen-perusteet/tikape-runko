package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {
    private Database database;
    
    public RaakaAineDao(Database database) {
        this.database=database;
    }
        @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM RaakaAine WHERE id=?");
        stmt.setInt(1, key);
        
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        RaakaAine r = new RaakaAine(rs.getInt("id"),rs.getString("nimi"));
        
        rs.close();
        stmt.close();
        
        con.close();
        return r;
    }
    
    @Override
    public List<RaakaAine> findAll() throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM RaakaAine");
        List<RaakaAine> aineet = new ArrayList();
        
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            RaakaAine r = new RaakaAine(rs.getInt("id"),rs.getString("nimi"));
            aineet.add(r);
        }
        rs.close();
        stmt.close();
        
        con.close();
                
        return aineet;
    }
    
    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
        if (object.getId()==null) {
            return save(object);
        }
        else {
            return update(object);
        }
    }
    
    @Override 
    public void delete(Integer key) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        stmt.setInt(1,key);
        stmt.executeUpdate();
        
        con.close();
    }
    
    private RaakaAine save(RaakaAine object) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("INSERT INTO RaakaAine"
                + " (nimi)"
                + " VALUES (?)");
        stmt.setString(1, object.getNimi());
        
        stmt.executeUpdate();
        stmt.close();
        
        stmt = con.prepareStatement("SELECT * FROM RaakaAine WHERE nimi=?");
        stmt.setString(1, object.getNimi());
        
        ResultSet rs = stmt.executeQuery();
        rs.next();
        
        object.setId(rs.getInt("id"));
        
        stmt.close();
        rs.close();
        
        con.close();
        
        return object;
    }
    
    private RaakaAine update(RaakaAine object) throws SQLException {
        Connection con = database.getConnection();
        
        PreparedStatement stmt = con.prepareStatement("UPDATE RaakaAine SET nimi=? WHERE id=?");
        stmt.setString(1, object.getNimi());
        stmt.setInt(2, object.getId());
        
        stmt.executeUpdate();
        
        stmt.close();
        
        con.close();
        
        return object;
    }
}
