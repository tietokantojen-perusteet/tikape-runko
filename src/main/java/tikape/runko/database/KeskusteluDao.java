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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Varaus");
        ResultSet rs = stmt.executeQuery();

        Map<Integer, List<Keskustelu>> alueidenKeskustelut = new HashMap<>();

        List<Keskustelu> varaukset = new ArrayList<>();

        while (rs.next()) {

            Integer varaustunnus = rs.getInt("varaustunnus");
            Timestamp alku = rs.getTimestamp("varaus_alkaa");
            Timestamp loppu = rs.getTimestamp("varaus_loppuu");

            Varaus v = new Varaus(varaustunnus, alku, loppu);
            varaukset.add(v);

            String pyora = rs.getString("pyora");

            if (!varaustenPyorat.containsKey(pyora)) {
                varaustenPyorat.put(pyora, new ArrayList<>());
            }
            varaustenPyorat.get(pyora).add(v);
        }

        rs.close();
        stmt.close();
        connection.close();

        for (Pyora pyora : this.pyoraDao.findAll()) {
            if (!varaustenPyorat.containsKey(pyora.getRekisterinumero())) {
                continue;
            }

            for (Varaus varaus : varaustenPyorat.get(pyora.getRekisterinumero())) {
                varaus.setPyora(pyora);
            }
        }

        return varaukset;
    }
}

@Override
        public List<Keskustelu> findRange(int first, int count) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    

}
