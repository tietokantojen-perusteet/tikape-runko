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
    
    public int keskustelunViestienmaara(Integer keskustelu_id) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) AS luku FROM Viesti WHERE viesti.omakeskustelu = ?");
        stmt.setObject(1, keskustelu_id);
        
        
        ResultSet rs = stmt.executeQuery();
        int vastaus = rs.getInt("luku");
       
        rs.close();
        stmt.close();
        connection.close();

        return vastaus;
    }
    public int alueenViestienmaara(Integer alueid) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE Keskustelu.omaalue = ?");
        stmt.setObject(1, alueid);
      
        
        ResultSet rs = stmt.executeQuery();
        int summa = 0;
        while(rs.next()){
            summa = summa + keskustelunViestienmaara(rs.getInt("keskustelu_id"));
        }
       
        rs.close();
        stmt.close();
        connection.close();

        return summa;
    } 
        
    public Date keskustelunUusin(Integer keskusteluid) throws SQLException{
       Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE Viesti.omakeskustelu = ? ");
       
        stmt.setObject(1, keskusteluid);
      
        Date aloitus = keskusdao.findOne(keskusteluid).getDate();
        
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()){
            return aloitus;
        }
        Date yrite = rs.getDate("paivamaara");
        
        while(rs.next()){
            Date haastaja = rs.getDate("paivamaara");
            if(yrite.before(haastaja)){
                yrite = haastaja;
            }
        }
        
        rs.close();
        stmt.close();
        connection.close();

        return yrite; 
        
    }
   public Date alueenUusin(Integer alueid) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE Keskustelu.omaalue = ? ");
       
        stmt.setObject(1, alueid);      
        
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()){
            return null;
        }
        Date yrite = keskustelunUusin(rs.getInt("keskustelu_id"));
        
        while(rs.next()){
            Date haastaja = keskustelunUusin(rs.getInt("keskustelu_id"));
            if(yrite.before(haastaja)){
                yrite = haastaja;
            }
        }
        
        rs.close();
        stmt.close();
        connection.close();

        return yrite;
    }
   
    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
}
