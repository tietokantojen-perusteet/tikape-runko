/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Viesti;
/**
 *
 * @author llmlks
 */
public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    
    public ViestiDao(Database db) {
        this.database = db;
    }
    
    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = " + key);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer avaus_id = rs.getInt("avaus_id");
        Timestamp aika = rs.getTimestamp("aika");
        String sisalto = rs.getString("sisalto");
        String nimimerkki = rs.getString("nimimerkki");

        Viesti o = new Viesti(id, avaus_id, aika, sisalto, nimimerkki);

        rs.close();
        stmt.close();
        connection.close();

        return o;    }

    @Override
    public List findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer avaus_id = rs.getInt("avaus_id");
            Timestamp aika = rs.getTimestamp("aika");
            String sisalto = rs.getString("sisalto");
            String nimimerkki = rs.getString("nimimerkki");

            viestit.add(new Viesti(id, avaus_id, aika, sisalto, nimimerkki));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE * FROM Viesti WHERE id = " + key);
        stmt.setObject(1, key);

        stmt.executeQuery();
        stmt.close();
        connection.close();
    }
    
}
