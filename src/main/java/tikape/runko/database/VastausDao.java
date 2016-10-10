/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;
import java.sql.*;
import java.util.List;
import tikape.runko.domain.Vastaus;
/**
 *
 * @author maijhuot
 */
public class VastausDao implements Dao<Vastaus, Integer> {
    
    private Database database;
    
    public VastausDao(Database database) {
        this.database = database;
    }
    
    public Vastaus findOne(Integer id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus WHERE id = ?");
        stmt.setObject(1, id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        id = rs.getInt("id");
        Integer avaus = rs.getInt("avaus");
        String teksti = rs.getString("teksti");

        Vastaus v = new Vastaus(id, avaus, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }
    
    @Override
    public List<Vastaus> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vastaus create(Vastaus t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Integer key, Vastaus t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
