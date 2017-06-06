package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Opiskelija;


public class AlueDao implements Dao<Alue, Integer>{
    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }
    
    public Alue create(Alue uusiAlue) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Alue (kuvaus) VALUES ( ? )");
        stmt.setObject(1, uusiAlue.getKuvaus());
        stmt.execute();
        
        stmt = connection.prepareStatement("SELECT Alue.alue_id AS id FROM Alue WHERE Alue.kuvaus = ? ;");       
        stmt.setObject(1, uusiAlue.getKuvaus());
        ResultSet rs = stmt.executeQuery();
        
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        int id = rs.getInt("id");
        System.out.println("UUsi ID ON : " + id);
        Alue luotuAlue = new Alue(id, uusiAlue.getKuvaus(), 0 , "");
        stmt.close();
        connection.close();        
        return luotuAlue;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT Alue.alue_id AS id, Alue.kuvaus AS kuvaus, "
                + "COUNT(Viesti.viesti_id) AS viesteja, MAX(Viesti.ajankohta) AS viimeisin "
                + "FROM Alue LEFT JOIN Aihe ON Alue.alue_id=Aihe.alue_id LEFT JOIN Viesti ON Aihe.aihe_id=Viesti.aihe_id " 
                + "WHERE Alue.alue_id = ? GROUP BY Alue.alue_id ORDER BY Alue.kuvaus;");       
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        int id = rs.getInt("id");
        String kuvaus = rs.getString("kuvaus");
        int viesteja = rs.getInt("viesteja");
        String viimeisin = rs.getString("viimeisin");

        Alue alue = new Alue(id, kuvaus, viesteja, viimeisin);

        rs.close();
        stmt.close();
        connection.close();

        return alue;    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT Alue.alue_id AS id, Alue.kuvaus AS kuvaus, "
                + "COUNT(Viesti.viesti_id) AS viesteja, MAX(Viesti.ajankohta) AS viimeisin "
                + "FROM Alue LEFT JOIN Aihe ON Alue.alue_id=Aihe.alue_id LEFT JOIN Viesti ON Aihe.aihe_id=Viesti.aihe_id " 
                + "GROUP BY Alue.alue_id ORDER BY Alue.kuvaus;");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String kuvaus = rs.getString("kuvaus");
            int viesteja = rs.getInt("viesteja");
            String viimeisin = rs.getString("viimeisin");

            alueet.add(new Alue(id, kuvaus, viesteja, viimeisin));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
