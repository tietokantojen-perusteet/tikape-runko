package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.Pragma;

public class Database<T> {

    private boolean debug;
    private String databaseAddress;
    
    public void setDebugMode(boolean d) {
        this.debug = d;
    }

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        SQLiteConfig sqLiteConfig = new SQLiteConfig();
        Properties properties = sqLiteConfig.toProperties();
        properties.setProperty(Pragma.DATE_STRING_FORMAT.pragmaName, "yyyy-MM-dd HH:mm:ss");
        
        return DriverManager.getConnection(databaseAddress, properties);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }
    
    public List<T> queryAndCollect(String query, Collector<T> col) throws SQLException {
        List<T> rows = new ArrayList<>();
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while(rs.next()) {
            if (debug) {
                System.out.println("---");
                System.out.println("query");
                debug(rs);
                System.out.println("---");
            }
            
            rows.add(col.collect(rs));
        }
        
        rs.close();
        stmt.close();
        return rows;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        return lista;
    }
    
    private void debug(ResultSet rs) throws SQLException {
        int columns = rs.getMetaData().getColumnCount();
        for (int i = 0; i < columns; i++) {
            System.out.print(
                    rs.getObject(i + 1) + ":"
                    + rs.getMetaData().getColumnName(i + 1) + "  ");
        }

        System.out.println();
    }
}
