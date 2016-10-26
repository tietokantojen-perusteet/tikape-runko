package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import tikape.runko.domain.*;

public class AlueDao implements Dao<Alue, Integer>{
    private Database database;
    
    public AlueDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Alue create(Alue a) throws SQLException {
        Connection conn = database.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Alue (nimi, kuvaus) VALUES (?, ?);", 
                Statement.RETURN_GENERATED_KEYS);
        
        stmt.setString(1, a.getNimi());
        stmt.setString(2, a.getKuvaus());
        
        stmt.executeUpdate();
        
        // Haetaan alueelle annettu avain ja asetetaan se olion tunnukseksi
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        a.setTunnus(rs.getInt(1));
        
        rs.close();
        stmt.close();
        conn.close();
        
        return a;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT al.*, MAX(lahetetty) as viimeisin_viesti, COUNT(*) as viestimaara FROM Alue al "
          + "LEFT JOIN Aihe ai ON al.tunnus = ai.alue "
          + "LEFT JOIN Viesti v ON ai.tunnus = v.aihe "
          + "WHERE al.tunnus = ? GROUP BY al.tunnus;"
        );
        
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            return null;
        }
        
        Alue alue = collect(rs);

        rs.close();
        stmt.close();
        connection.close();

        return alue;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT al.*, MAX(lahetetty) as viimeisin_viesti, COUNT(*) as viestimaara FROM Alue al "
          + "LEFT JOIN Aihe ai ON al.tunnus = ai.alue "
          + "LEFT JOIN Viesti v ON ai.tunnus = v.aihe "
          + "GROUP BY al.tunnus ORDER BY LOWER(al.nimi);"
        );
        
        ResultSet rs = stmt.executeQuery();
        
        List<Alue> alueet = new ArrayList();
        
        while (rs.next()) {
            alueet.add(collect(rs));
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        return alueet;
    }
    
    public List<Alue> findAllIn(Collection<Integer> keys) throws SQLException {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        // Luodaan IN-kyselyä varten paikat, joihin arvot asetetaan
        StringBuilder arvot = new StringBuilder("?");
        for (int i = 1; i < keys.size(); i++) {
            arvot.append(", ?");
        }

        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT al.*, MAX(lahetetty) as viimeisin_viesti, COUNT(*) as viestimaara FROM Alue al "
          + "LEFT JOIN Aihe ai ON al.tunnus = ai.alue "
          + "LEFT JOIN Viesti v ON ai.tunnus = v.aihe "
          + "WHERE al.tunnus IN (" + arvot + ") = ? GROUP BY al.tunnus;"
        );
        
        int laskuri = 1;
        
        for (Integer key : keys) {
            stmt.setInt(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        
        while (rs.next()) {
            alueet.add(collect(rs));
        }
        
        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }
    
    @Override
    public void update(Integer key, Alue t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Alue collect(ResultSet rs) throws SQLException {
        Integer tunnus = rs.getInt("tunnus");
        String nimi = rs.getString("nimi");
        String kuvaus = rs.getString("kuvaus");
        Timestamp viimViesti = rs.getTimestamp("viimeisin_viesti");
        Integer viestimaara = rs.getInt("viestimaara");
        if (viimViesti == null) {
            viestimaara = 0;
        }

        return new Alue(tunnus, nimi, kuvaus, viimViesti, viestimaara);
    }
}
