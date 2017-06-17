/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Viesti;

/**
 *
 * @author Kim
 */
public class ViestiDao implements Dao<Viesti, Integer> {
    
    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
            String kayttaja = rs.getString("kayttaja");
            String teksti = rs.getString("teksti");
            Date paivamaara = rs.getDate("paivamaara");
            Integer ketjuid = rs.getInt("ketju");

            Viesti v = new Viesti(id, kayttaja, teksti, paivamaara, ketjuid);

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
            Integer id = rs.getInt("id");
            String kayttaja = rs.getString("kayttaja");
            String teksti = rs.getString("teksti");
            java.sql.Date paivamaara = rs.getDate("paivamaara");
            Integer ketjuid = rs.getInt("ketju");

            viestit.add(new Viesti(id, kayttaja, teksti, paivamaara, ketjuid));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
    public List<Viesti> findAllForKetjuId(int ketjunId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            if (rs.getInt("ketju") == ketjunId) {
                Integer id = rs.getInt("id");
                String kayttaja = rs.getString("kayttaja");
                String teksti = rs.getString("teksti");
                Date paivamaara = rs.getDate("paivamaara");
                Integer ketjuid = rs.getInt("ketju");
            
                viestit.add(new Viesti(id, kayttaja, teksti, paivamaara, ketjuid));
            }
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
    public void lisaaViesti(String kayttaja, String teksti, int ketju) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement(
                                    "INSERT INTO Viesti "
                                    + "(kayttaja, teksti, paivamaara, ketju) "
                                    + "VALUES (?, ?, DATETIME('now', 'localtime'), ?)");
        
        stmnt.setString(1, kayttaja);
        stmnt.setString(2, teksti);
        stmnt.setInt(3, ketju);

        stmnt.execute();

        stmnt.close();
        connection.close();
}

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
