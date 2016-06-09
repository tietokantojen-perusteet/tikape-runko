
package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.aneere.Dao;
import tikape.aneere.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

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
