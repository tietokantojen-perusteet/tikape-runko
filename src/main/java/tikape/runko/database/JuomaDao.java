/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tikape.runko.domain.Juoma;

public class JuomaDao implements Dao<Juoma, Integer> {

    private Database db;
    private Connection conn;
    private int seuraavaId;

    public JuomaDao(Connection conn) {
        this.conn = conn;
        this.seuraavaId = 20;
    }

    @Override
    public Juoma findOne(Integer key) throws SQLException {
        
        PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM Juoma WHERE id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        Juoma uusi = new Juoma(rs.getInt("id"), rs.getString("nimi"), rs.getString("valmistusOhje"));
        stmt.close();
        rs.close();
//        conn.close();
        return uusi;
    }

    public int vapaaId() throws SQLException {
        return findAll().size() + 1;
    }

    @Override
    public List<Juoma> findAll() throws SQLException {
    
        List lista = new ArrayList();
        
        PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM Juoma");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new Juoma(rs.getInt("id"), rs.getString("nimi"), rs.getString("valmistusOhje")));
        }
        stmt.close();
        rs.close();
//        conn.close();
        return lista;
    }

    public List getAinekset(List listaaaa) {
        List lista = new ArrayList();

        return lista;
    }

    public Juoma saveOrUpdate(Juoma object) throws SQLException, Exception {
        this.seuraavaId++;
        int iid = this.seuraavaId;
        PreparedStatement stmt
                = this.conn.prepareStatement("INSERT INTO Juoma (id, nimi,valmistusohje) VALUES (?,?,?)");
        stmt.setInt(1, iid);
        stmt.setString(2, object.getNimi());
        stmt.setString(3, object.getValmistusOhje());
        stmt.executeUpdate();
        Juoma palautus = findOne(object.getId());
//        conn.close();
        this.seuraavaId++;
        return palautus;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM Juoma WHERE juoma.id =(?)");
        stmt.setInt(1, key);
        stmt.executeUpdate();
    }



}
