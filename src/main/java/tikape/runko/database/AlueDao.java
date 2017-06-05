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

    @Override
    public Alue findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT Alue.Alue_id AS id, Alue.Kuvaus AS kuvaus, "
                + "COUNT(Viesti.viesti_id) AS viesteja, MAX(Viesti.ajankohta) AS viimeisin "
                + "FROM Alue LEFT JOIN Aihe ON Alue.alue_id=Aihe.alue_id LEFT JOIN Viesti ON Aihe.aihe_id=Viesti.aihe_id " 
                + "GROUP BY Alue.alue_id ORDER BY Alue.kuvaus;");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String kuvaus = rs.getString("kuvaus");
            Integer viesteja = rs.getInt("viesteja");
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
