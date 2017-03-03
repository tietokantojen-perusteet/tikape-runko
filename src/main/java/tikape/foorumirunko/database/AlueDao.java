/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.foorumirunko.database;

import java.sql.*;
import java.util.*;
import tikape.foorumirunko.domain.Alue;

/**
 *
 * @author eemitant
 * @author xvixvi
 */
public class AlueDao implements Dao<Alue, Integer> {
    
    private Database database;
    
    public AlueDao(Database d) {
        database = d;
    }
    
    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Alue WHERE alueen_id = ?");
        stmt.setObject(1, key);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = Integer.parseInt(rs.getString("alueen_id"));
        String nimi = rs.getString("alueen_nimi");

        Alue a = new Alue(id, nimi);

        rs.close();
        stmt.close();
        con.close();

        return a;    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");
        ResultSet rs = stmt.executeQuery();

        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("alueen_id"));
            String nimi = rs.getString("alueen_nimi");

            alueet.add(new Alue(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;    }
    
}
