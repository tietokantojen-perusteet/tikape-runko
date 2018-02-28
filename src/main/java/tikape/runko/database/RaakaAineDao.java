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
import tikape.runko.domain.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database db;
    private Connection conn;
    private int seuraavaId;

    public RaakaAineDao(Connection conn) {
        this.conn = conn;
        this.seuraavaId = 10;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {

        PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        RaakaAine uusi = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
        stmt.close();
        rs.close();
//        conn.close();
        return uusi;
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        List lista = new ArrayList();

        PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM RaakaAine");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new RaakaAine(rs.getInt("id"), rs.getString("nimi")));
        }
        stmt.close();
        rs.close();
//        conn.close();
        return lista;
    }

    public int vapaaId() throws SQLException {
        this.seuraavaId++;
        return this.seuraavaId;
    }

    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException, Exception {
//        this.seuraavaId++;
        int iid = this.seuraavaId;
        PreparedStatement stmt
                = this.conn.prepareStatement("INSERT INTO RaakaAine (id, nimi) VALUES (?,?)");
        stmt.setInt(1, object.getId());
        stmt.setString(2, object.getNimi());

        stmt.executeUpdate();
        RaakaAine palautus = findOne(object.getId());
//        conn.close();
        
        return palautus;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM RaakaAine WHERE raakaAine.id =(?)");
        stmt.setInt(1, key);
        stmt.executeUpdate();
    }

}
