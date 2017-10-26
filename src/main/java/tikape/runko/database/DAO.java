package tikape.runko.database;

import java.sql.*;
import java.util.*;

public interface DAO<T, K> {
    
    void create(T object) throws SQLException;
    
    T read(K key) throws SQLException;
    
    List<T> readAll() throws SQLException;

    void delete(K key) throws SQLException;
}
