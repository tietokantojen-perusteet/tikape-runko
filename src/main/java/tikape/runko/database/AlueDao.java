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
import tikape.runko.domain.Alue;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Alue o = new Alue(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Alue> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            alueet.add(new Alue(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    public Integer getLukumaara(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) AS viestit FROM Viesti, Aihe, Alue WHERE Viesti.aihe_id = Aihe.id AND Aihe.alue_id = Alue.id AND Alue.id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        int palauta = rs.getInt("viestit");
        rs.close();
        stmt.close();
        connection.close();

        return palauta;
    }

    public String getViimeisin(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT Viesti.time FROM Viesti, Aihe, Alue WHERE Viesti.aihe_id = Aihe.id AND Aihe.alue_id = Alue.id AND Alue.id = " + key + " ORDER BY Viesti.time DESC LIMIT 1");

        ResultSet rs = stmt.executeQuery();

        if (rs.isClosed()) {
            return "ei viesteja";
        }

        String palauta = rs.getString("time");

        rs.close();
        stmt.close();
        connection.close();

        return palauta;

    }

    public void create(String nimi) throws SQLException {
        Connection connection = database.getConnection();
        
        nimi = nimi.trim(); //poistaa välilyönnit
        if (nimi.isEmpty()) { //varmistaa, ettei voi luoda nimetöntä aluetta
            return;
        }
        
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Alue (nimi) VALUES ('" + nimi + "')");

        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }
    

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}
