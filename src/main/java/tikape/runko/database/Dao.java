package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.domain.Alue;

public interface Dao<T, K> {

    T create(T t) throws SQLException;

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;

    void update(K key, T t) throws SQLException;

    public List<T> findAllIn(Collection<K> keys) throws SQLException;

}
