
package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Smoothie;

public class SmoothieDAO implements DAO<Smoothie, Integer> {
    private final Database database;
    
    public SmoothieDAO(Database database) {
        this.database = database;
    }

    @Override
    public void create(Smoothie object) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("");
        // TODO:
        
    }

    @Override
    public Smoothie read(Integer key) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Smoothie where id = ?;");
        statement.setInt(1, key);
        // TODO:
        
        return null;
    }

    @Override
    public List<Smoothie> readAll() throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Smoothie;");
        // TODO:
        
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
