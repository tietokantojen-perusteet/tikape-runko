package tikape.runko.database;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import tikape.runko.domain.Keskustelu;
import tikape.runko.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private Dao<Keskustelu, Integer> keskusteluDao;

    public ViestiDao(Database database, Dao<Keskustelu, Integer> keskusteluDao) {
        this.database = database;
        this.keskusteluDao = keskusteluDao;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        return (Viesti) database.queryAndCollect("SELECT * FROM Viesti WHERE id = ?",
                rs -> new Viesti(rs.getInt("id"),
                        this.keskusteluDao.findOne(rs.getInt("keskustelu_id")),
                        rs.getTimestamp("aika", Calendar.getInstance(TimeZone.getTimeZone("UTC"))),
                        rs.getString("kayttaja"),
                        rs.getString("sisalto")),
                key).get(0);
    }

    public List<Viesti> findKeskustelunViestitSivullinen(int keskustelu_id, int sivunumero, int viestienLkmSivulla) throws SQLException {
        Keskustelu keskustelu = keskusteluDao.findOne(keskustelu_id);
        return database.queryAndCollect(
                "SELECT * FROM Viesti WHERE keskustelu_id = ? ORDER BY id LIMIT ? OFFSET ?",
                rs -> new Viesti(rs.getInt("id"),
                        keskustelu,
                        rs.getTimestamp("aika", Calendar.getInstance(TimeZone.getTimeZone("UTC"))),
                        rs.getString("kayttaja"),
                        rs.getString("sisalto")),
                keskustelu_id, viestienLkmSivulla, (sivunumero - 1) * viestienLkmSivulla);
    }

    public List<Viesti> findKeskustelunViestitSivullinenPlusYlimaaraiset(int keskustelu_id, int sivunumero, int viestienLkmSivulla, int ylimaaraistenViestienMaara) throws SQLException {
        Keskustelu keskustelu = keskusteluDao.findOne(keskustelu_id);
        return database.queryAndCollect(
                "SELECT * FROM Viesti WHERE keskustelu_id = ? ORDER BY id LIMIT ? OFFSET ?",
                rs -> new Viesti(rs.getInt("id"),
                        keskustelu,
                        rs.getTimestamp("aika", Calendar.getInstance(TimeZone.getTimeZone("UTC"))),
                        rs.getString("kayttaja"),
                        rs.getString("sisalto")),
                keskustelu_id, viestienLkmSivulla + ylimaaraistenViestienMaara, (sivunumero - 1) * viestienLkmSivulla);
    }

    public List<Viesti> findKeskustelunViestitKaikki(int keskustelu_id) throws SQLException {
        Keskustelu keskustelu = keskusteluDao.findOne(keskustelu_id);
        return database.queryAndCollect(
                "SELECT * FROM Viesti WHERE keskustelu_id = ? ORDER BY id",
                rs -> new Viesti(rs.getInt("id"),
                        keskustelu,
                        rs.getTimestamp("aika", Calendar.getInstance(TimeZone.getTimeZone("UTC"))),
                        rs.getString("kayttaja"),
                        rs.getString("sisalto")),
                keskustelu_id);
    }

    public void lisaaViesti(int keskustelu_id, String kayttaja, String sisalto) throws SQLException {
        database.update("INSERT INTO Viesti (keskustelu_id, kayttaja, sisalto) VALUES (?, ?, ?)",
                keskustelu_id, kayttaja, sisalto);
    }
}
