package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Keskustelu;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;
    private Dao<Alue, Integer> alueDao;

    public KeskusteluDao(Database database, Dao<Alue, Integer> alueDao) {
        this.database = database;
        this.alueDao = alueDao;
    }

    public Keskustelu findOne(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String otsikko = rs.getString("otsikko");

        Keskustelu keskustelu = new Keskustelu(id, otsikko);

        Integer alue = rs.getInt("alue_id");

        rs.close();
        stmt.close();
        connection.close();

        keskustelu.setAlue(this.alueDao.findOne(alue));

        return keskustelu;
    }

    @Override
    public List<Keskustelu> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu");
        ResultSet rs = stmt.executeQuery();

        Map<Integer, List<Keskustelu>> alueidenKeskustelut = new HashMap<>();

        List<Keskustelu> keskustelut = new ArrayList<>();

        while (rs.next()) {

            Integer id = rs.getInt("id");
            String otsikko = rs.getString("otsikko");

            Keskustelu keskustelu = new Keskustelu(id, otsikko);
            keskustelut.add(keskustelu);

            int alue = rs.getInt("alue_id");

            if (!alueidenKeskustelut.containsKey(alue)) {
                alueidenKeskustelut.put(alue, new ArrayList<>());
            }
            alueidenKeskustelut.get(alue).add(keskustelu);
        }

        rs.close();
        stmt.close();
        connection.close();

        for (Alue alue : this.alueDao.findAll()) {
            if (!alueidenKeskustelut.containsKey(alue.getId())) {
                continue;
            }

            for (Keskustelu keskustelu : alueidenKeskustelut.get(alue.getId())) {
                keskustelu.setAlue(alue);
            }
        }

        return keskustelut;
    }

    public List<Keskustelu> findAlueenKeskustelut(int alue_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT Keskustelu.*, COUNT(Viesti.id) AS viestienLkm, MAX(Viesti.aika) AS viimeisinAika "
                + "FROM Keskustelu LEFT JOIN Viesti ON Keskustelu.id = Viesti.keskustelu_id "
                + "WHERE Keskustelu.alue_id = ? "
                + "GROUP BY Keskustelu.id");
        stmt.setObject(1, alue_id);
        ResultSet rs = stmt.executeQuery();

        Alue alue = alueDao.findOne(alue_id);

        List<Keskustelu> alueenKeskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String otsikko = rs.getString("otsikko");
            Integer viestienLkm = rs.getInt("viestienLkm");
            Timestamp viimeisinAika = rs.getTimestamp("viimeisinAika");

            alueenKeskustelut.add(new Keskustelu(id, alue, otsikko, viestienLkm, viimeisinAika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueenKeskustelut;
    }

//    @Override
//    public List<Keskustelu> findRange(int first, int count) throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
