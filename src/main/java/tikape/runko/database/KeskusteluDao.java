package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Keskustelu;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;
    private Dao<Alue, Integer> alueDao;

    public KeskusteluDao(Database database, Dao<Alue, Integer> alueDao) {
        this.database = database;
        this.alueDao = alueDao;
    }

    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        return (Keskustelu) database.queryAndCollect("SELECT * FROM Keskustelu WHERE id = ?",
                rs -> new Keskustelu(rs.getInt("id"),
                        this.alueDao.findOne(rs.getInt("alue_id")),
                        rs.getString("otsikko"), null, null),
                key).get(0);
    }

    public List<Keskustelu> findAlueenKeskustelutUusimmat(int alue_id) throws SQLException {
        int uusimpienLkm = 11;
        Alue alue = alueDao.findOne(alue_id);
        return database.queryAndCollect(
                "SELECT Keskustelu.*, COUNT(Viesti.id) AS viestienLkm, MAX(Viesti.aika) AS viimeisinAika "
                + "FROM Keskustelu LEFT JOIN Viesti ON Keskustelu.id = Viesti.keskustelu_id "
                + "WHERE Keskustelu.alue_id = ? "
                + "GROUP BY Keskustelu.id ORDER BY viimeisinAika DESC LIMIT ?",
                rs -> new Keskustelu(rs.getInt("id"),
                        alue,
                        rs.getString("otsikko"),
                        rs.getInt("viestienLkm"),
                        rs.getTimestamp("viimeisinAika", Calendar.getInstance(TimeZone.getTimeZone("UTC")))),
                alue_id, uusimpienLkm);
    }

    public List<Keskustelu> findAlueenKeskustelutKaikki(int alue_id) throws SQLException {
        Alue alue = alueDao.findOne(alue_id);
        return database.queryAndCollect(
                "SELECT Keskustelu.*, COUNT(Viesti.id) AS viestienLkm, MAX(Viesti.aika) AS viimeisinAika "
                + "FROM Keskustelu LEFT JOIN Viesti ON Keskustelu.id = Viesti.keskustelu_id "
                + "WHERE Keskustelu.alue_id = ? "
                + "GROUP BY Keskustelu.id ORDER BY viimeisinAika DESC",
                rs -> new Keskustelu(rs.getInt("id"),
                        alue,
                        rs.getString("otsikko"),
                        rs.getInt("viestienLkm"),
                        rs.getTimestamp("viimeisinAika", Calendar.getInstance(TimeZone.getTimeZone("UTC")))),
                alue_id);
    }

    public void lisaaKeskustelunavaus(int alue_id, String otsikko, String kayttaja, String sisalto) throws SQLException {
        // Käyttäen tietokantatransaktiota, jottei kahden sql-komennon välissä tietokantaan ilmesty toista uutta keskustelua.
        Connection connection = database.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement stmt1 = connection.prepareStatement(
                "INSERT INTO Keskustelu (alue_id, otsikko) VALUES (?, ?)");
        PreparedStatement stmt2 = connection.prepareStatement(
                "INSERT INTO Viesti (keskustelu_id, kayttaja, sisalto) VALUES ((SELECT MAX(id) FROM Keskustelu), ?, ?)");
        stmt1.setInt(1, alue_id);
        stmt1.setString(2, otsikko);
        stmt2.setString(1, kayttaja);
        stmt2.setString(2, sisalto);

        stmt1.execute();
        stmt2.execute();
        connection.commit();

        connection.setAutoCommit(true);
        stmt1.close();
        stmt2.close();
        connection.close();
    }
}
