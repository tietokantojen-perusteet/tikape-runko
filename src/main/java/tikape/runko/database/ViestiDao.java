/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aihealue;
import tikape.runko.domain.Viesti;
import tikape.runko.domain.Ketju;

/**
 *
 * @author onniaarn
 */
public class ViestiDao implements Dao<Viesti, Integer> {
    private Database db;
    
    public ViestiDao(Database database) {
        this.db = database;
    }
    
    public void add(Viesti v) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Viesti (ketju, kommentti, kayttajanimi) VALUES (?, ?, ?);");
        stmt.setObject(1, v.getKetju_id());
        stmt.setObject(2, v.getKommentti());
        stmt.setObject(3, v.getKayttajanimi());
//        stmt.setObject(4, v.getAika());
        stmt.executeUpdate();
        
        stmt.close();
        conn.close();
    };

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        return new Viesti(null,null,null,null);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection conn = db.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Viesti");
        List<Viesti> viestit = new ArrayList();
        while (rs.next()) {
            Viesti v = new Viesti(rs.getInt("ketju"), rs.getString("kommentti"), rs.getString("kayttajanimi"));
            viestit.add(v);
        }
        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // JATKOKEHITETTÄVÄÄ
//        Connection conn = db.getConnection();
//        PreparedStatement stmt = conn.prepareStatement("DELETE FFROM Viesti WHERE ")
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Viesti> findByKetju(Ketju k) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Viesti WHERE ketju = ? ORDER BY aika ASC");
        stmt.setObject(1, k.getKetju());
        List<Viesti> viestit = new ArrayList();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Viesti viesti = new Viesti(rs.getInt("ketju"), rs.getString("kommentti"), rs.getString("kayttajanimi"));
            viestit.add(viesti);
        }
        return viestit;
    }
    
    public Viesti findByAlue(Aihealue a) {
        // Ketjuissa ei ole viittausta Alueeseen joten tätä ei voida toteuttaa
//        Connection conn = db.getConnection();
//        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Viesti, WHERE ")
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
//    public Viesti findMostRecent() throws SQLException {
//        Connection conn = db.getConnection();
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM Viesti ORDER BY aika DESC LIMIT 1");
//        
//        Boolean hasOne = rs.next();
//        if (!hasOne) {
//            return null;
//        }
//        
//        Viesti v = new Viesti(rs.getInt("ketju"), rs.getString("kommentti"), rs.getString("kayttajanimi"));
//        return v;
//    }
    
//    public String getAika(Integer key) throws SQLException {
//        Connection connection = db.getConnection();
//        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE ketju = ? ");
//        stmt.setInt(1, key);
//
//        ResultSet rs = stmt.executeQuery();
//
//        if (!rs.next()) {
//            return null;
//        }
//
//        String aika = rs.getString("aika");
//
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return aika;
//    }
    
    public Viesti findMostRecentByKetju(Ketju k) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Viesti WHERE ketju = ? ORDER BY aika DESC LIMIT 1");
        stmt.setObject(1, k.getKetju());
        ResultSet rs = stmt.executeQuery();
        
        Boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        Viesti v = new Viesti(rs.getInt("ketju"), rs.getString("kommentti"), rs.getString("kayttajanimi"));
        return v;
    }
}
