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
 * @author Kim
 */
public class KetjuDao implements Dao<Ketju, Integer> {

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

        Integer id = rs.getInt("id");
        String otsikko = rs.getString("aihe");
        Integer alueid = rs.getInt("alue");

        Ketju k = new Ketju(id, otsikko, alueid);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Ketju> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju");

        ResultSet rs = stmt.executeQuery();
        List<Ketju> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String otsikko = rs.getString("otsikko");
            Integer alueid = rs.getInt("alue");
            
            alueet.add(new Ketju(id, otsikko, alueid));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }
    
    public void lisaaKetju(String otsikko, int alue) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Ketju (otsikko, alue) "
                + "VALUES (?, ?)");
        stmt.setString(1, otsikko);
        stmt.setInt(2, alue);
        stmt.execute();
        
        stmt.close();
        connection.close();
    }

    
    public List<Ketju> findAllForAlueId(int alueenId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju");

        ResultSet rs = stmt.executeQuery();
        List<Ketju> ketjut = new ArrayList<>();
        while (rs.next()) {
            if (rs.getInt("alue") == alueenId) {
                Integer id = rs.getInt("id");
                String otsikko = rs.getString("otsikko");
                Integer alueid = rs.getInt("alue");
            
                ketjut.add(new Ketju(id, otsikko, alueid));
            }
        }

        rs.close();
        stmt.close();
        connection.close();

        return ketjut;
    }
    
    
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
