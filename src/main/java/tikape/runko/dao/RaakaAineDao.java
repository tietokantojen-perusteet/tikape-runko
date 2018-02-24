package tikape.runko.dao;
import tikape.runko.domain.*;
import java.sql.*;
import java.util.*;
import tikape.runko.database.Database;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {
    private Database database;
    
    public RaakaAineDao(Database database){
        this.database = database;
    }
    
    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        RaakaAine r = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
        stmt.close();
        rs.close();
        conn.close();

        return r;
    }
    
    @Override
    public List<RaakaAine> findAll() throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT * FROM RaakaAine;");
        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        
        while (rs.next()){
            RaakaAine r = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
            raakaAineet.add(r);
        }
        
        stmt.close();
        rs.close();
        conn.close();
        
        return raakaAineet;
    }
    
    @Override
    public void delete(Integer key) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    
    @Override
        public RaakaAine saveOrUpdate(RaakaAine raakaAine) throws SQLException{
        if (findOne(raakaAine.getId()) == null){
            return save(raakaAine);
        } else {
            return update(raakaAine);
        }
    }
    
    public RaakaAine save(RaakaAine raakaAine) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine "
                + "(id, nimi"
                + "VALUES (?, ?");
        stmt.setInt(1, raakaAine.getId());
        stmt.setString(2, raakaAine.getNimi());
        stmt.executeUpdate();
        stmt.close();
        
        stmt = conn.prepareStatement("SELECT * FROM RaakaAine"
                                + " WHERE id = ? AND nimi = ?");
        stmt.setInt(1, raakaAine.getId());
        stmt.setString(2, raakaAine.getNimi());

        ResultSet rs = stmt.executeQuery();
        rs.next();

        RaakaAine r = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
        stmt.close();
        rs.close();
        conn.close();
        
        return r;
    }
    
    public RaakaAine update(RaakaAine raakaAine) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE RaakaAine SET"
                + "nimi = ? WHERE id = ?");
        stmt.setString(1, raakaAine.getNimi());
        stmt.setInt(2, raakaAine.getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return raakaAine;
    }
    
    public void poistaRaakaAine(RaakaAine raakaAine, AnnosDao aDao, AnnosRaakaAineDao araDao) throws SQLException{
        //Poistaa raaka-aineen sekä raakaaine taulusta, että annosraakaaine - taulusta
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE raakaAineId = ?");
        stmt.setInt(1, raakaAine.getId());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        
        for (Annos a : aDao.findAll()) {
            araDao.poistaRaakaAineAnnoksesta(raakaAine, a.getId());
        }
    }

    
}
