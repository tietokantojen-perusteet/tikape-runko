/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaakaAineDao implements Dao<RaakaAine, Integer>{
    private Database db;
    
    public RaakaAineDao(Database db){
        this.db = db;
    }
    
    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection conn = this.db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        RaakaAine uusi = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
        stmt.close();
        rs.close();
        conn.close();
        return uusi;    
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
    List lista = new ArrayList();
        Connection conn = this.db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new RaakaAine(rs.getInt("id"), rs.getString("nimi")));
        }
        stmt.close();
        rs.close();
        conn.close();
        return lista;
    }

    @Override
    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}