package ryhmatyo.reseptiarkisto;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;

    T saveOrUpdate(T object) throws SQLException;

    void delete(K key) throws SQLException;
}

