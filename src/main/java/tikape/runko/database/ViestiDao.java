/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Viesti;

/**
 *
 * @author onniaarn
 */
public class ViestiDao implements Dao<Viesti, Integer> {
    private Database db;
    
    public ViestiDao(Database database) {
        this.db = database;
    }
    
    public Viesti create(Viesti v) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Viesti VALUES (?, ?, ?, ?);");
        stmt.setObject(1, v.getKetju_id());
        stmt.setObject(2, v.getKommentti());
        stmt.setObject(3, v.getKayttajanimi());
        stmt.setObject(4, v.getAika());
        stmt.executeUpdate();
        return v;
    };
    
    public 

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection conn = db.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Viesti");
        List<Viesti> viestit = new ArrayList();
        while (rs.next()) {
            Viesti v = new Viesti(rs.getInt("ketju_id"), rs.getString("kommentti"), rs.getString("kayttajanimi"), rs.getTimestamp("aika"));
            
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Viesti WHERE ketju_id = ?");
        stmt.setObject(1, k.getKetju());
        List<Viesti> viestit = new ArrayList();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Viesti viesti = new Viesti(rs.getInt("ketju_id"), rs.getString("kommentti"), rs.getString("kayttajanimi"), rs.getTimestamp("aika"));
            viestit.add(viesti);
        }
        return viestit;
    }
}
