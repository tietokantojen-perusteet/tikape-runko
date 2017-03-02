package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Keskustelualue;

public class KeskustelualueDao implements Dao<Keskustelualue, Integer> {
    
    private Database database;

    public KeskustelualueDao(Database uusiDatabase) {
        this.database = uusiDatabase;
    }

    @Override
    public Keskustelualue findOne(Integer key) throws SQLException {
        
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelualue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String aihe = rs.getString("aihe");

        Keskustelualue ka = new Keskustelualue(id, aihe);

        rs.close();
        stmt.close();
        connection.close();

        return ka;
    }

    @Override
    public List<Keskustelualue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelualue");

        ResultSet rs = stmt.executeQuery();
        List<Keskustelualue> keskustelualueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String aihe = rs.getString("aihe");

            keskustelualueet.add(new Keskustelualue(id, aihe));
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelualueet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt1 = connection.prepareStatement("DELETE FROM Viesti WHERE alue = ?");
        PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM Keskustelunavaus WHERE alue = ?");
        PreparedStatement stmt3 = connection.prepareStatement("DELETE FROM Keskustelualue WHERE alue = ?");
        
        stmt1.setObject(1, key);
        stmt2.setObject(1, key);
        stmt3.setObject(1, key);
        
        stmt1.execute();
        stmt2.execute();
        stmt3.execute();
    }    
}
