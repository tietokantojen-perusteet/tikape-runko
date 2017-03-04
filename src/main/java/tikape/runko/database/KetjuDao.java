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
public class KetjuDao implements Dao<Ketju, Integer>{
    private Database database;
    
    public KetjuDao(Database database) {
        this.database = database;
    }

    @Override
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
        String aihe = rs.getString("aihe");
        String otsikko = rs.getString("otsikko");
        String sisalto = rs.getString("sisalto");
        String aloitusaika = rs.getString("aloitusaika");
        String kayttajanimi = rs.getString("kayttajanimi");

        Ketju k = new Ketju(ketju, aihe, otsikko, sisalto, aloitusaika, kayttajanimi);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Ketju> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju ORDER BY aloitusaika ASC");

        ResultSet rs = stmt.executeQuery();
        List<Ketju> ketjut = new ArrayList<>();
        while (rs.next()) {
            
            Integer ketju = rs.getInt("ketju");
            String aihe = rs.getString("aihe");
            String otsikko = rs.getString("otsikko");
            String sisalto = rs.getString("sisalto");
            String aloitusaika = rs.getString("aloitusaika");
            String kayttajanimi = rs.getString("kayttajanimi");
            
            ketjut.add(new Ketju(ketju, aihe, otsikko, sisalto, aloitusaika, kayttajanimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ketjut;
    }
    
    @Override
        public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Ketju WHERE ketju = ?");
        stmt.setInt(1, key);
        
        stmt.executeUpdate();
        
        stmt.close();
        connection.close();
    }

        public void add(Ketju p) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Ketju (ketju, aihe, otsikko, sisalto, aloitusaika, kayttajanimi) VALUES (?)");
        stmt.setInt(1, p.getKetju());
        stmt.setString(2, p.getAihe());
        stmt.setString(3, p.getOtsikko());
        stmt.setString(4, p.getSisalto());
        stmt.setString(5, p.getAloitusaika());
        stmt.setString(6, p.getKayttajanimi());
        stmt.executeUpdate();
        
        stmt.close();
        connection.close();
    }


}

