
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
