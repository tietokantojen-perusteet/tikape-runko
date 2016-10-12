package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database<T> {
    private String address;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.address = databaseAddress;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(address);
    }

    public int update(String updateQuery, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(updateQuery);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        int changes = stmt.executeUpdate();

        stmt.close();
        conn.close();

        return changes;
    }

    public List<T> queryAndCollect(String query, Collector<T> col, Object... params) throws SQLException {
        Connection conn = getConnection();

        List<T> rows = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            rows.add(col.collect(rs));
        }

        rs.close();
        stmt.close();
        conn.close();

        return rows;
    }
}