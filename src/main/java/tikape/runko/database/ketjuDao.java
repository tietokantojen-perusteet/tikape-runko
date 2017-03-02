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
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Ketju;

/**
 *
 * @author aleksimu
 */
public class ketjuDao implements Dao<Ketju, Integer>{
    private Database database;
    
    public ketjuDao(Database database) {
        this.database = database;
    }

    
    public Ketju findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer ketju = rs.getInt("ketju");
        String nimi = rs.getString("nimi");
        String otsikko = rs.getString("otsikko");
        String sisalto = rs.getString("sisalto");

        Ketju k = new Ketju(ketju, nimi, otsikko, sisalto);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

 
    public List<Ketju> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju");

        ResultSet rs = stmt.executeQuery();
        List<Ketju> ketjut = new ArrayList<>();
        while (rs.next()) {
            Integer ketju = rs.getInt("ketju");
            String nimi = rs.getString("nimi");
            String otsikko = rs.getString("otsikko");
            String sisalto = rs.getString("sisalto");
            
            ketjut.add(new Ketju(ketju, nimi, otsikko, sisalto));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ketjut;
    }

   
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}


