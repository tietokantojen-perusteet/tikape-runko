package tikape.runko.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T create(T uusi) throws SQLException;
    
    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;
    
    //List<T> findAllIn(K key) throws SQLException;

    void delete(K key) throws SQLException;
}
