package tikape.runko.database;

import java.sql.*;
import java.util.List;
import tikape.runko.domain.Vastaus;

public class VastausDao implements Dao<Vastaus, Integer> {
    private Database database;

    //Konstruktori
    
    public VastausDao(Database database) {
        this.database = database;
    }

    //Dao-metodit
    
    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        KeskustelunavausDao keskustelunavausdao = new KeskustelunavausDao(database);

        return (Vastaus) database.queryAndCollect("SELECT * FROM Vastaus WHERE id = ?", rs -> new Vastaus(rs.getInt("id"), keskustelunavausdao.findOne(rs.getInt("avaus")), rs.getString("teksti"), rs.getString("ajankohta"), rs.getString("kirjoittaja")), key).get(0);
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        KeskustelunavausDao keskustelunavausdao = new KeskustelunavausDao(database);

        return database.queryAndCollect("SELECT * FROM Vastaus", rs -> new Vastaus(rs.getInt("id"), keskustelunavausdao.findOne(rs.getInt("avaus")), rs.getString("teksti"), rs.getString("ajankohta"), rs.getString("kirjoittaja")));
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        database.update("DELETE FROM Vastaus WHERE id = ?", key);
    }

    @Override
    public Vastaus create(Vastaus t) throws SQLException {
        database.update("INSERT INTO Vastaus (avaus, teksti, kirjoittaja) VALUES (?, ?, ?)", t.getAvaus().getId(), t.getTeksti(), t.getKirjoittaja());
        return findByParameters(t.getAvaus().getId(), t.getTeksti(), t.getKirjoittaja()); // saadaan oikeat arvot sarakkeisiin 'id' ja 'ajankohta' -- toivottavasti...
    }
    
    @Override
    public void update(Integer key, Vastaus t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //Mitä tämä voisi tehdä, kun vastauksilla ei ole otsikkoa?
    }
    
    //Muut metodit
    
    public Vastaus findByParameters(Integer avaus, String teksti, String kirjoittaja) throws SQLException {
        KeskustelunavausDao keskustelunavausdao = new KeskustelunavausDao(database);
        
        return (Vastaus) database.queryAndCollect("SELECT * FROM Vastaus WHERE avaus = ? AND teksti = ? AND kirjoittaja = ?", rs -> new Vastaus(rs.getInt("id"), keskustelunavausdao.findOne(rs.getInt("avaus")), rs.getString("teksti"), rs.getString("ajankohta"), rs.getString("kirjoittaja")), avaus, teksti, kirjoittaja).get(0);
    }
}