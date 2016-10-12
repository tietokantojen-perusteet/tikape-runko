package tikape.runko.database;

import java.sql.SQLException;
import java.util.List;
import tikape.runko.domain.Avausnakyma;
import tikape.runko.domain.Keskustelualue;

public class KeskustelualueDao implements Dao<Keskustelualue, Integer> {
    private Database database;
    
    //Konstruktori

    public KeskustelualueDao(Database database) {
        this.database = database;
    }
    
    //Dao-metodit

    @Override
    public Keskustelualue findOne(Integer key) throws SQLException {
        return (Keskustelualue) database.queryAndCollect("SELECT * FROM Keskustelualue WHERE id = ?", rs -> new Keskustelualue(rs.getInt("id"), rs.getString("aihealue"), rs.getString("kuvaus"), rs.getString("perustettu"), rs.getString("perustaja")), key).get(0);
    }

    @Override
    public List<Keskustelualue> findAll() throws SQLException {
        return database.queryAndCollect("SELECT * FROM Keskustelualue", rs -> new Keskustelualue(rs.getInt("id"), rs.getString("aihealue"), rs.getString("kuvaus"), rs.getString("perustettu"), rs.getString("perustaja")));
    }

    @Override
    public void delete(Integer key) throws SQLException {
        database.update("DELETE FROM Keskustelualue WHERE id = ?", key);
    }

    @Override
    public Keskustelualue create(Keskustelualue t) throws SQLException {
        database.update("INSERT INTO Keskustelualue (aihealue, kuvaus, perustaja) VALUES (?, ?, ?)", t.getAihealue(), t.getKuvaus(), t.getPerustaja());
        return findByName(t.getAihealue()); //aihealue sarake oli määritelty UNIQUE-parametrilla!
    }

    @Override
    public void update(Integer key, Keskustelualue t) throws SQLException {
        database.update("UPDATE Keskustelualue SET aihealue = ? WHERE id = ?", t.getAihealue(), key);
    }
    
    //Muut metodit
    
    public Keskustelualue findByName(String name) throws SQLException {
        return (Keskustelualue) database.queryAndCollect("SELECT * FROM Keskustelualue WHERE aihealue = ?", rs -> new Keskustelualue(rs.getInt("id"), rs.getString("aihealue"), rs.getString("kuvaus"), rs.getString("perustettu"), rs.getString("perustaja")), name).get(0);
    }
    
    public Integer findNoOfReplies(Integer key) throws SQLException {
        String query = "SELECT COUNT(Vastaus.id) FROM Keskustelualue LEFT JOIN Keskustelunavaus ON Keskustelualue.id = Keskustelunavaus.alue AND Keskustelualue.id = ? INNER JOIN Vastaus ON Keskustelunavaus.id = Vastaus.avaus";
        
        return (Integer) database.queryAndCollect(query, rs -> rs.getInt("COUNT(Vastaus.id)"), key).get(0);
    }
    
    public String timeOfLastReply(Integer key) throws SQLException { // Ei huomioi keskustelunavauksen aloitusajankohtaa
        String query = "SELECT Vastaus.ajankohta FROM Keskustelualue INNER JOIN Keskustelunavaus ON Keskustelualue.id = Keskustelunavaus.alue AND Keskustelualue.id = ? INNER JOIN Vastaus ON Keskustelunavaus.id = Vastaus.avaus ORDER BY Vastaus.ajankohta DESC LIMIT 1";
        
        return (String) database.queryAndCollect(query, rs -> rs.getInt("Vastaus.ajankohta"), key).get(0);
    }
    
    public List<Avausnakyma> openingView() throws SQLException {
        String query = "SELECT Keskustelualue.id AS Id, Keskustelualue.aihealue AS Alue, COUNT(DISTINCT Keskustelunavaus.id) + COUNT(DISTINCT Vastaus.id) AS Viesteja_yhteensa, MAX(MAX(Vastaus.ajankohta), MAX(Keskustelunavaus.aloitettu)) AS Viimeisin_viesti FROM Keskustelualue LEFT JOIN Keskustelunavaus ON Keskustelualue.id = Keskustelunavaus.alue LEFT JOIN Vastaus ON Keskustelunavaus.id = Vastaus.avaus GROUP BY Keskustelualue.id ORDER BY Keskustelualue.aihealue";
        
        return database.queryAndCollect(query, rs -> new Avausnakyma(Integer.parseInt(rs.getString("Id")), rs.getString("Alue"), Integer.parseInt(rs.getString("Viesteja_yhteensa")), rs.getString("Viimeisin_viesti")));
    }
}