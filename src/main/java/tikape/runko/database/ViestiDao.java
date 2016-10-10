package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.*;


public class ViestiDao implements Dao<Viesti, Integer> {
    private Database database;
    private Dao<Aihe, Integer> aiheDao;
    
    public ViestiDao(Database database, Dao<Aihe, Integer> aiheDao) {
        this.database = database;
        this.aiheDao = aiheDao;
    }

    @Override
    public Viesti create(Viesti v) throws SQLException {
        Connection conn = database.getConnection();
        String sql = "INSERT INTO Viesti (aihe, teksti, lahettaja, lahetetty) " 
                   + "VALUES (?, ?, ?, ?);";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        stmt.setInt(1, v.getAihe().getTunnus());
        stmt.setString(2, v.getTeksti());
        stmt.setString(3, v.getLahettaja());
        stmt.setTimestamp(4, v.getLahetetty());
        
        stmt.executeUpdate();
        
        // Haetaan viestille annettu avain ja asetetaan se olion tunnukseksi
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        v.setTunnus(rs.getInt(1));
        
        rs.close();
        stmt.close();
        conn.close();
        
        return v;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        
        if (!hasOne) {
            return null;
        }

        Integer aiheTunnus = rs.getInt("aihe");
        String teksti = rs.getString("teksti");
        String lahettaja = rs.getString("lahettaja");
        Timestamp lahetetty = rs.getTimestamp("lahetetty");

        Viesti v = new Viesti(key, teksti, lahettaja, lahetetty);
        v.setAihe(aiheDao.findOne(aiheTunnus));
        
        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Integer key, Viesti t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
