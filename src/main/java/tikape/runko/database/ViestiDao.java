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
import java.util.Date;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Keskustelu;
import tikape.runko.domain.Viesti;

/**
 *
 * @author tamella
 */
// tämä luokka koostuu niistä kyselyistä, jotka taululle Viesti voidaan esittää
public class ViestiDao implements Dao<Viesti, Integer>{
    private Database database;
    private Dao<Keskustelu, Integer> keskusdao;
    
    public ViestiDao(Database base, Dao<Keskustelu, Integer> keskusdao ){
        this.database = base;
        this.keskusdao = keskusdao;
    }
    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE viesti_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("viesti_id");
        Date paivamaara = rs.getDate("julkaisuaika");
        String kirjoittaja = rs.getString("kirjoittaja");
        String teksti = rs.getString("teksti");
        
        

        Viesti v = new Viesti(id, paivamaara, kirjoittaja, teksti);
        
        Integer omakeskustelu = rs.getInt("omakeskustelu");
        
        v.setOmakeskustelu(this.keskusdao.findOne(omakeskustelu));
        
        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("viesti_id");
            Date paivamaara = rs.getDate("julkaisuaika");
            String kirjoittaja = rs.getString("kirjoittaja");
            String teksti = rs.getString("teksti");
            
            Integer omakes = rs.getInt("omakeskustelu");
            
            

            Viesti tekstiteksti = new Viesti(id, paivamaara, kirjoittaja, teksti);
            tekstiteksti.setOmakeskustelu(this.keskusdao.findOne(omakes));
            viestit.add(tekstiteksti);
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
}
