package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE tunnus = ?");
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

        Viesti viesti = new Viesti(key, teksti, lahettaja, lahetetty);
        viesti.setAihe(aiheDao.findOne(aiheTunnus));
        
        rs.close();
        stmt.close();
        connection.close();

        return viesti;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti;");
        ResultSet rs = stmt.executeQuery();
        
        Map<Integer, List<Viesti>> aiheidenViestit = new HashMap<>();
        
        List<Viesti> viestit = new ArrayList<>();
        
        while (rs.next()) {
            Integer tunnus = rs.getInt("tunnus");
            Integer aiheTunnus = rs.getInt("aihe");
            String teksti = rs.getString("teksti");
            String lahettaja = rs.getString("lahettaja");
            Timestamp lahetetty = rs.getTimestamp("lahetetty");
            
            Viesti viesti = new Viesti(tunnus, teksti, lahettaja, lahetetty);
            viestit.add(viesti);
            
            if (!aiheidenViestit.containsKey(aiheTunnus)) {
                aiheidenViestit.put(aiheTunnus, new ArrayList<>());
            }
            
            aiheidenViestit.get(aiheTunnus).add(viesti);
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        for (Aihe aihe : this.aiheDao.findAllIn(aiheidenViestit.keySet())) {
            for (Viesti viesti : aiheidenViestit.get(aihe.getTunnus())) {
                viesti.setAihe(aihe);
            }
        }

        return viestit;
    }

    @Override
    public void update(Integer key, Viesti t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> findAllIn(Collection<Integer> keys) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
