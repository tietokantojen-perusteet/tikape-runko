package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Keskustelunavaus;

public class KeskustelunavausDao implements Dao<Keskustelunavaus, Integer> {
    private Database database;

    //Konstruktori
    
    public KeskustelunavausDao(Database database) {
        this.database = database;
    }
    
    //Muut metodit
    
    public List<Keskustelunavaus> findLastTenOpenings(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        
        String query = "SELECT * FROM Keskustelunavaus INNER JOIN Vastaus ON Keskustelunavaus.id = Vastaus.avaus INNER JOIN Keskustelualue ON Keskustelunavaus.alue = Keskustelualue.id AND Keskustelualue.id = ? GROUP BY Keskustelunavaus.id ORDER BY Vastaus.ajankohta DESC LIMIT 10";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);
        
        ResultSet rs = stmt.executeQuery();
        List<Keskustelunavaus> avaukset = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer alue = rs.getInt("alue");
            String otsikko = rs.getString("otsikko");
            String avausteksti = rs.getString("avaus");
            String aloitettu = rs.getString("aloitettu");
            String aloittaja = rs.getString("aloittaja");
            
            avaukset.add(new Keskustelunavaus(id, alue, otsikko, avausteksti, aloitettu, aloittaja));
        }

        rs.close();
        stmt.close();
        connection.close();

        return avaukset;
    }
    
    public Integer findNoOfReplies(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        
        String query = "SELECT COUNT(Vastaus.id) FROM Keskustelunavaus LEFT JOIN Vastaus ON Vastaus.avaus = Keskustelunavaus.id AND Keskustelunavaus.id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);
        
        ResultSet rs = stmt.executeQuery();
        
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        Integer lukuMaara = rs.getInt("COUNT(Vastaus.id)");
            
        rs.close();
        stmt.close();
        connection.close();

        return lukuMaara;
    }
    
    //Dao-metodit

    @Override
    public Keskustelunavaus findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelunavaus WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer alue = rs.getInt("alue");
        String otsikko = rs.getString("otsikko");
        String avausteksti = rs.getString("avaus");
        String aloitettu = rs.getString("aloitettu");
        String aloittaja = rs.getString("aloittaja");

        Keskustelunavaus avaus = new Keskustelunavaus(id, alue, otsikko, avausteksti, aloitettu, aloittaja);

        rs.close();
        stmt.close();
        connection.close();

        return avaus;
    }

    @Override
    public List<Keskustelunavaus> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelunavaus");

        ResultSet rs = stmt.executeQuery();
        List<Keskustelunavaus> avaukset = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer alue = rs.getInt("alue");
            String otsikko = rs.getString("otsikko");
            String avausteksti = rs.getString("avaus");
            String aloitettu = rs.getString("aloitettu");
            String aloittaja = rs.getString("aloittaja");
            
            avaukset.add(new Keskustelunavaus(id, alue, otsikko, avausteksti, aloitettu, aloittaja));
        }

        rs.close();
        stmt.close();
        connection.close();

        return avaukset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Keskustelunavaus WHERE id = ?");
        stmt.setObject(1, key);
        stmt.close();
        connection.close();
    }
}