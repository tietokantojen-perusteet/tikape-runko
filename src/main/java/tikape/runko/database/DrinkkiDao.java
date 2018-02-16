/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.*;
import tikape.runko.domain.Drinkki;
import java.util.*;

/**
 *
 * @author Dennis
 */
public class DrinkkiDao implements Dao {
    
    private Database database;
    
    public DrinkkiDao(Database database) {
        this.database = database;
    }

    @Override
    public Object findOne(Object key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Drinkki drinkki = new Drinkki(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return drinkki;
    }

    @Override
    public List findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki");

        ResultSet rs = stmt.executeQuery();
        List<Drinkki> drinkit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            
            drinkit.add(new Drinkki(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return drinkit;
    }

    @Override
    public void delete(Object key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Opiskelija WHERE id = ?");

        stmt.setObject(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    
}
