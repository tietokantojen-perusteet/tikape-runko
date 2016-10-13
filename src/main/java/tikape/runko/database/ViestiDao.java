package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Timestamp aika = rs.getTimestamp("aika");
        String kayttaja = rs.getString("kayttaja");
        String sisalto = rs.getString("sisalto");

        Viesti viesti = new Viesti(id, aika, kayttaja, sisalto);

        Integer keskustelu = rs.getInt("keskustelu_id");

        rs.close();
        stmt.close();
        connection.close();

        viesti.setKeskustelu(this.keskusteluDao.findOne(keskustelu));

        return viesti;
    }

    @Override
    public List findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Viesti> findKeskustelunViestit(int keskustelu_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE keskustelu_id = ? ORDER BY id");
        stmt.setObject(1, keskustelu_id);
        ResultSet rs = stmt.executeQuery();

        Keskustelu keskustelu = keskusteluDao.findOne(keskustelu_id);

        List<Viesti> keskustelunViestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Timestamp aika = rs.getTimestamp("aika");
            String kayttaja = rs.getString("kayttaja");
            String sisalto = rs.getString("sisalto");

            keskustelunViestit.add(new Viesti(id, keskustelu, aika, kayttaja, sisalto));
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelunViestit;
    }

//    @Override
//    public List findRange(int first, int count) throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
