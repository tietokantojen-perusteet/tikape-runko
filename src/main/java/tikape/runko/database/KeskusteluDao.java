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
import tikape.runko.domain.Alue;
import tikape.runko.domain.Keskustelu;
import java.util.Date;

/**
 *
 * @author tamella
 */
// tämä luokka koostuu niistä kyselyistä, joita taululle Keskustelut voidaan esittää

public class KeskusteluDao implements Dao<Keskustelu, Integer>{
    private Database database;
    private AlueDao aluedao;
    
    public KeskusteluDao(Database base, AlueDao aluedao ){
        this.database = base;
        this.aluedao = aluedao;
    }
    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE keskustelu_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer keskusteluid = rs.getInt("keskustelu_id");
        String otsikko = rs.getString("otsikko");
        String aloittaja = rs.getString("aloittaja");
        String aloitusviesti = rs.getString("aloitusviesti");
        Timestamp paivamaara = rs.getTimestamp("paivamaara");

        Keskustelu k = new Keskustelu(keskusteluid, otsikko, aloittaja, aloitusviesti, paivamaara);
        
        Integer omaalue = rs.getInt("omaalue");
        
        k.setOmaalue(this.aluedao.findOne(omaalue));
        
        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Keskustelu> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu");

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer keskusteluid = rs.getInt("keskustelu_id");
            String otsikko = rs.getString("otsikko");
            String aloittaja = rs.getString("aloittaja");
            String aloitusviesti = rs.getString("aloitusviesti");
            Integer omaa = rs.getInt("omaalue");
            Timestamp paivamaara = rs.getTimestamp("paivamaara");
//            Date paivamaara = rs.getDate("paivamaara");

            Keskustelu palapala = new Keskustelu(keskusteluid, otsikko, aloittaja, aloitusviesti, paivamaara);
            palapala.setOmaalue(this.aluedao.findOne(omaa));
            keskustelut.add(palapala);
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelut;
    }
    public void lisaaKeskustelu( Alue alue, String otsikko, String aloittaja, String aloitusviesti ) throws SQLException{
         Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO Keskustelu (omaalue, otsikko, aloittaja, aloitusviesti) VALUES ('"+alue+","+otsikko+ ","+aloittaja+","+aloitusviesti+"')");

        conn.close();
    }
    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    public AlueDao getAlueDao(){
        return this.aluedao;
    }
}
