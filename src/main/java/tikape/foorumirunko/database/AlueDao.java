/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.foorumirunko.database;

import java.sql.SQLException;
import java.util.List;
import tikape.foorumirunko.domain.Alue;

/**
 *
 * @author eemitant
 * @author xvixvi
 */
public class AlueDao implements Dao<Alue, Integer> {
    
    private Database database;
    
    public AlueDao(Database d) {
        database = d;
    }
    
    @Override
    public Alue findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
