package tikape.runko.database;

import tikape.runko.database.Database;
import tikape.runko.domain.Aihealue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AihealueDao implements Dao<Aihealue, Integer> {
    
    private Database database;
    
    public AihealueDao(Database database) throws Exception {
        this.database = database;
    }
    
    private List<Aihealue> collect(ResultSet rs) throws Exception {
        ArrayList<Aihealue> collection = new ArrayList<>();
        
        while (rs.next()) {
            Aihealue p = new Aihealue(rs.getInt("aihealue_id"),rs.getString("aihe"));
            
            collection.add(p);
        }
        
        return collection;
    }
    
    @Override
    public Aihealue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue WHERE aihealue_id = ?");
        stmt.setInt(1, key);
        
        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            return null;
        }
        
        Aihealue p = new Aihealue(rs.getInt("aihealue_id"),rs.getString("aihe"));
        
        rs.close();
        stmt.close();
        connection.close();
        
        return p;
    }
    
    @Override
    public List<Aihealue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue ORDER BY aihealue_id ASC");
        ResultSet rs = stmt.executeQuery();
        
        List<Aihealue> collection = null;
        
        collect: try {
            collection = collect(rs);
        } catch (Exception e) {
            break collect;
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        return collection;
    }
    

    public void add(Aihealue p) throws SQLException {
   try (Connection conn = database.getConnection()) {
            Statement st = conn.createStatement();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO aihealue (aihe) VALUES (?)");
            stmt.setObject(1, p.getAihe());
            stmt.executeUpdate();
            stmt.close();
            conn.close();

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    @Override
        public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Aihealue WHERE aihealue_id = ?");
        stmt.setInt(1, key);
        
        stmt.executeUpdate();
        
        stmt.close();
        connection.close();
    }


}