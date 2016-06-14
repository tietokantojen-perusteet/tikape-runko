
package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.*;
import tikape.runko.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {
    private Database database;
    public ViestiDao(Database database) {
        this.database = database;
    }
    public void lisaaViesti(String viesti) {
        String lisattava = "";
        lisattava = "INSERT INTO Viesti (sisalto) VALUES ('" + viesti + "');";        
        
        try (Connection conn = database.getConnection()) {
            Statement st = conn.createStatement();
            
            st.executeUpdate(lisattava);

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
        
    }
    @Override
    public Viesti findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
