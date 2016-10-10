package tikape.runko.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;

    //void delete(K key) throws SQLException;
    List<T> findRange(int first, int count) throws SQLException;

}
