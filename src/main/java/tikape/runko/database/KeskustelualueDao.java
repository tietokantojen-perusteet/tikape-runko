package tikape.runko.database;

import java.sql.SQLException;
import java.util.List;
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
}