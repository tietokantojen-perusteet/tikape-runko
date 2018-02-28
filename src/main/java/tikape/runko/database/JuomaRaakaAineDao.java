/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Juoma;
import tikape.runko.domain.JuomaRaakaAine;
import tikape.runko.domain.RaakaAine;

public class JuomaRaakaAineDao implements Dao<JuomaRaakaAine, Integer> {

    private Database database;
    private Connection conn;

    public JuomaRaakaAineDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public JuomaRaakaAine findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<JuomaRaakaAine> findAll() throws SQLException {
        List lista = new ArrayList();

        PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM JuomaRaakaAine");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new JuomaRaakaAine(rs.getString("juomaNimi"), rs.getString("raakaAineNimi"), rs.getString("maara")));
        }
        stmt.close();
        rs.close();
//        conn.close();
        return lista;
    }

    public List<JuomaRaakaAine> findJuomanPerusteella(Juoma ju) throws SQLException {
        List lista = new ArrayList();

        PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM JuomaRaakaAine WHERE JuomaRaakaAine.juomaNimi=(?);");
        stmt.setString(1, ju.getNimi());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new JuomaRaakaAine(rs.getString("juomaNimi"), rs.getString("raakaAineNimi"), rs.getString("maara")));
        }
        lista.add(new JuomaRaakaAine("", "", ""));
        lista.add(new JuomaRaakaAine("", "", ""));
        
        stmt.close();
        rs.close();
//        conn.close();
        return lista;
    }

    public List<JuomaRaakaAine> findRaakaAineenPerusteella(RaakaAine ra) throws SQLException {
        List lista = new ArrayList();

        PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM JuomaRaakaAine WHERE JuomaRaakaAine.raakaAineNimi=(?)");
        stmt.setString(1, ra.getNimi());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new JuomaRaakaAine(rs.getString("juomaNimi"), rs.getString("raakaAineNimi"), rs.getString("maara")));
        }
        lista.add(new JuomaRaakaAine("", "", ""));
        lista.add(new JuomaRaakaAine("", "", ""));
        stmt.close();
        rs.close();
//        conn.close();
        return lista;
    }

    public JuomaRaakaAine saveOrUpdate(JuomaRaakaAine object) throws SQLException {
        PreparedStatement stmt
                = this.conn.prepareStatement("INSERT INTO JuomaRaakaAine (juomaNimi, raakaAineNimi, maara) VALUES (?,?,?)");
        stmt.setString(1, object.getJuoma());
        stmt.setString(2, object.getRaakAine());
        stmt.setString(3, object.getMaara());

        stmt.executeUpdate();
        JuomaRaakaAine palautus = new JuomaRaakaAine(object.getJuoma(), object.getRaakAine(), object.getMaara());
//        conn.close();
        return palautus;

    }

    public void deleteJuomanPerusteella(Juoma juoma) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM JuomaRaakaAine WHERE JuomaRaakaAine.juomaNimi =(?)");
        stmt.setString(1, juoma.getNimi());
        stmt.executeUpdate();
//        List<JuomaRaakaAine> lista = findAll();
//        for (JuomaRaakaAine ra : lista) {
//            if (ra.getJuoma().equals(juoma.getNimi())) {
//                deleteJRA(ra);
//            }
//        }

    }

    public void deleteRaakaAineenPerusteella(RaakaAine ra) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM JuomaRaakaAine WHERE JuomaRaakaAine.raakaAineNimi =(?)");
        stmt.setString(1, ra.getNimi());
        stmt.executeUpdate();
//        List<JuomaRaakaAine> lista = findAll();
//        for (JuomaRaakaAine jra : lista) {
//            if (jra.getRaakAine().equals(ra.getNimi())) {
//                deleteJRA(jra);
//            }
//        }
        
    }

    public void deleteJRA(JuomaRaakaAine jra) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM JuomaRaakaAine WHERE JuomaRaakaAine.juomaNimi =(?) AND JuomaRaakaAine.raakaAineNimi =(?)");
        stmt.setString(1, jra.getJuoma());
        stmt.setString(2, jra.getRaakAine());
        stmt.executeUpdate();
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
