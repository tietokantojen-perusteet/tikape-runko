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
        KeskustelualueDao keskustelualuedao = new KeskustelualueDao(database);
        String query = "SELECT * FROM Keskustelunavaus INNER JOIN Vastaus ON Keskustelunavaus.id = Vastaus.avaus INNER JOIN Keskustelualue ON Keskustelunavaus.alue = Keskustelualue.id AND Keskustelualue.id = ? GROUP BY Keskustelunavaus.id ORDER BY Vastaus.ajankohta DESC LIMIT 10";
        
        return database.queryAndCollect(query, rs -> new Keskustelunavaus(rs.getInt("id"), keskustelualuedao.findOne(rs.getInt("alue")), rs.getString("otsikko"), rs.getString("avaus"), rs.getString("aloitettu"), rs.getString("aloittaja")), key);
    }
    
    public Integer findNoOfReplies(Integer key) throws SQLException {
        KeskustelualueDao keskustelualuedao = new KeskustelualueDao(database);
        String query = "SELECT COUNT(Vastaus.id) FROM Keskustelunavaus LEFT JOIN Vastaus ON Vastaus.avaus = Keskustelunavaus.id AND Keskustelunavaus.id = ?";
        
        return (Integer) database.queryAndCollect(query, rs -> rs.getInt("COUNT(Vastaus.id)"), key).get(0);
    }
    
    //Dao-metodit

    @Override
    public Keskustelunavaus findOne(Integer key) throws SQLException {
        KeskustelualueDao keskustelualuedao = new KeskustelualueDao(database);
        
        return (Keskustelunavaus) database.queryAndCollect("SELECT * FROM Keskustelunavaus WHERE id = ?", rs -> new Keskustelunavaus(rs.getInt("id"), keskustelualuedao.findOne(rs.getInt("alue")), rs.getString("otsikko"), rs.getString("avaus"), rs.getString("aloitettu"), rs.getString("aloittaja")), key).get(0);
    }
    
    @Override
    public List<Keskustelunavaus> findAll() throws SQLException {
        KeskustelualueDao keskustelualuedao = new KeskustelualueDao(database);
        
        return database.queryAndCollect("SELECT * FROM Keskustelunavaus", rs -> new Keskustelunavaus(rs.getInt("id"), keskustelualuedao.findOne(rs.getInt("alue")), rs.getString("otsikko"), rs.getString("avaus"), rs.getString("aloitettu"), rs.getString("aloittaja")));
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        database.update("DELETE FROM Keskustelunavaus WHERE id = ?", key);
    }

    public Keskustelunavaus create(Keskustelunavaus t) throws SQLException {
        database.update("INSERT INTO Keskustelunavaus (alue, otsikko, avaus, aloittaja) VALUES (?, ?, ?, ?)", t.getAlue().getId(), t.getOtsikko(), t.getAvaus(), t.getAloittaja());
        return findByName(t.getOtsikko());
    }
    
    public Keskustelunavaus findByName(String name) throws SQLException {
        KeskustelualueDao keskustelualuedao = new KeskustelualueDao(database);
        
        return (Keskustelunavaus) database.queryAndCollect("SELECT * FROM Keskustelunavaus WHERE name = ?", rs -> new Keskustelunavaus(rs.getInt("id"), keskustelualuedao.findOne(rs.getInt("alue")), rs.getString("otsikko"), rs.getString("avaus"), rs.getString("aloitettu"), rs.getString("aloittaja")), name).get(0);
    }

    @Override
    public void update(Integer key, Keskustelunavaus t) throws SQLException {
        database.update("UPDATE Keskustelunavaus SET name = ? WHERE id = ?", t.getOtsikko(), key);
    }
}