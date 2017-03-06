package tikape.runko.database;

import tikape.runko.database.Database;
import tikape.runko.domain.Aihealue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AihealueDao implements Dao<Aihealue, String> {
    
    private Database database;
    
    public AihealueDao(Database database) throws Exception {
        this.database = database;
    }
    
    private List<Aihealue> collect(ResultSet rs) throws Exception {
        ArrayList<Aihealue> collection = new ArrayList<>();
        
        while (rs.next()) {
            Aihealue p = new Aihealue(rs.getString("aihe"));
            
            collection.add(p);
        }
        
        return collection;
    }
    
    @Override
    public Aihealue findOne(String key) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue WHERE aihe = ?");
        stmt.setString(1, key);
        
        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            return null;
        }
        
        Aihealue p = new Aihealue(rs.getString("aihe"));
        
        rs.close();
        stmt.close();
        connection.close();
        
        return p;
    }
    
//    public Integer viestienMaara(String key) throws SQLException {
//        Connection connection = database.getConnection();
//        
//        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM Aihealue, Ketju, Viesti WHERE Aihealue.aihe = ? AND Ketju.ketju = Aihealue.aihe AND Viesti.ketju = Ketju.ketju");
//        stmt.setString(1, key);
//        
//        ResultSet rs = stmt.executeQuery();
//        
//        if (!rs.next()) {
//            return null;
//        }
//        
//        Integer pal = Integer.parseInt(rs.getString(1));
//        
//        rs.close();
//        stmt.close();
//        connection.close();
//        
//        return pal;
//    }
    
//    public String viimeisinViesti() {
//        
//        ArrayList<Timestamp> al = new ArrayList<>();
//        
//        for (Ketju k : kDao) {
//            
//        }
//        return "";
//    }
    
    @Override
    public List<Aihealue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue ORDER BY aihe ASC");
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
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Aihealue (aihe) VALUES (?)");
        stmt.setString(1, p.getAihe());
        stmt.executeUpdate();
        
        stmt.close();
        connection.close();
    }

    @Override
        public void delete(String key) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Aihealue WHERE aihe = ?");
        stmt.setString(1, key);
        
        stmt.executeUpdate();
        
        stmt.close();
        connection.close();
    }


}