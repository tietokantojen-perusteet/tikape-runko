package tikape.runko.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T create(T uusi) throws SQLException;
    
    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;
    
    // Jyrki muokkasi ti palaverin jälkeen
    List<T> findAllIn(K id) throws SQLException;

    void delete(K key) throws SQLException;
}
