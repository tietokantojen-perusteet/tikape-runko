package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
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
    
    public void addOne(Integer key, String aihe) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viesti VALUES (id = ?, aihe = ?)");
        
        stmt.setObject(1, key);
        stmt.setObject(2, aihe);
        
        stmt.execute();
        
        stmt.close();
        connection.close();
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
        
        stmt1.close();
        stmt2.close();
        stmt3.close();
        connection.close();
    }
    
    // Tämän metodin avulla saadaan etusivulle keskustelualueittain avauksien ja
    // viestien lukumäärät ja uusimpien viestien lähetysajankohdat
    public List<String[]> lukumaaratPerKA() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT "
                + "Keskustelualue.aihe AS aihe, "
                + "Count (Viesti.avaus) AS avauksia, "
                + "Count (*) AS viesteja, "
                + "MAX (Viesti.aika) AS uusin "
                + "FROM Keskustelualue JOIN Viesti "
                + "ON Keskustelualue.id = Viesti.alue "
                + "GROUP BY Viesti.alue");
        
        ResultSet rs = stmt.executeQuery();
        List<String[]> keskustelualueet = new ArrayList<>();
        
        while (rs.next()) {
            String aihe = rs.getString("aihe");
            String avauksia = rs.getString("avauksia");
            String viesteja = rs.getString("viesteja");
            long uusin = rs.getLong("uusin");
            
            // timestampin luomisessa saattaa joutua kertomaan 1000:lla tai ei, riippuu, talletetaanko ms vai s
            Date timestamp = new Date(uusin);
            String uusinStr = timestamp.toString();
            
            String[] alueenTiedot = new String[4];
            alueenTiedot[0] = aihe;
            alueenTiedot[1] = avauksia;
            alueenTiedot[2] = viesteja;
            alueenTiedot[3] = uusinStr;
            
            keskustelualueet.add(alueenTiedot);
        }

        rs.close();
        stmt.close();
        connection.close();
        
        return keskustelualueet;
    }
    
    public String getIdByTopic(String topic) throws SQLException {
        Connection connection = database.getConnection();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Keskustelualue");
        while (rs.next()) {
            if (rs.getString("aihe").equals(topic)) {
                return rs.getString("id");
            }
        }
        return null;
    }
}
