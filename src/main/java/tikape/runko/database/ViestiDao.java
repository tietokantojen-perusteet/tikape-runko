
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aihe;
import tikape.runko.domain.Viesti;


public class ViestiDao implements Dao<Viesti, Integer>{
    Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Viesti> aiheenViestit(int aihe_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM VIESTI "
                + "WHERE Viesti.aihe_id = ? ORDER BY Viesti.ajankohta;");
        
        stmt.setObject(1, aihe_id);
        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("viesti_id");
            String teksti = rs.getString("teksti");
            String nimimerkki = rs.getString("nimimerkki");
            String ajankohta = rs.getString("ajankohta");

            viestit.add(new Viesti(id, aihe_id, teksti, ajankohta, nimimerkki));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;        
    }    

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
            
            
}
