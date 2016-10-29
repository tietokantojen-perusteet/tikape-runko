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
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aihe;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    public List<Viesti> findAiheesta(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE aihe_id = ?");
        stmt.setObject(1, key);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
//        if (!hasOne) {
//            return null;
//        }

        AiheDao ad = new AiheDao(this.database);
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {

            Aihe aihe = ad.findOne(rs.getInt("aihe_id"));
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            String text = rs.getString("text");
            String time = rs.getString("time");

            Viesti o = new Viesti(aihe, id, nimi, text, time);
            viestit.add(o);
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
    public void create(String aiheid, String nimi, String text) throws SQLException {
        
        nimi = nimi.trim(); //ylimääräiset välilyönnit pois
        text = text.trim();
        
        if (nimi.isEmpty() || text.isEmpty()) { //jos nimi- tai viestikentissä ei ole mitään, viestiä ei luoda
            return;
        }
        
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viesti (aihe_id, nimi, text) VALUES (?, ?, ?)");
        stmt.setObject(1, aiheid);
        stmt.setObject(2, nimi);
        stmt.setObject(3, text);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }
    
    //Viestien määrä 10 per sivu ja tuorein ylimpänä EI TOIMI VIELÄ!!!
    public List<Viesti> findKymmenenViimeisintaViestia (Integer aiheid, Integer sivunro) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE aihe_id = ? "
                + "ORDER BY Viesti.time DESC LIMIT 10 OFFSET ((( ? -1)*10));");
        
        stmt.setInt(1, aiheid);
        stmt.setInt(2, sivunro);
        
        ResultSet rs = stmt.executeQuery();
        
        List<Viesti> viestit = new ArrayList<>();
        AiheDao ad = new AiheDao(this.database);

    while (rs.next()) {
        Integer id = rs.getInt("id");
        String time = rs.getString("time");
        String text = rs.getString("text");
        String nimi = rs.getString("nimi");
        Aihe aihe =  ad.findOne(rs.getInt("aihe_id"));

        Viesti o = new Viesti(aihe, id, nimi, text, time);
        viestit.add(o);
    }

    rs.close();
    stmt.close();
    connection.close();

    return viestit;     
    }
    
    //Poistaa viestin
    public void poista(String id) throws Exception {
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        try {
            Integer.parseInt(id);
        } catch (Throwable t) {
            return;
        }

        statement.execute("DELETE FROM Viesti WHERE id = " + id);
        connection.close();
    }
    
    public String getAiheId(String id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT aihe_id FROM Viesti WHERE id = ?");
        
        stmt.setString(1, id);
        
        ResultSet rs = stmt.executeQuery();

        String aiheid = rs.getString("aihe_id");

        rs.close();
        stmt.close();
        connection.close();

        return aiheid;
    }
    

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
