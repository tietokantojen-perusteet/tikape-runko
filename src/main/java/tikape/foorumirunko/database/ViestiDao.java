/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.foorumirunko.database;

import java.sql.SQLException;
import java.util.List;
import tikape.foorumirunko.domain.Viesti;

/**
 *
 * @author eemitant
 * @authro xvixvi
 */
public class ViestiDao implements Dao<Viesti, String> {
    
    private Database database;
    
    public ViestiDao(Database d) {
        database = d;
    }

    @Override
    public Viesti findOne(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
