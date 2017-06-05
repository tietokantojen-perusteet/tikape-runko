
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aihe;
import tikape.runko.domain.Alue;


public class AiheDao implements Dao<Aihe, Integer>{
    private Database database;

    public AiheDao(Database database) {
        this.database = database;
    }

    @Override
    public Aihe findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Aihe> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Aihe> alueenAiheet(int alue_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT Aihe.aihe_id AS id, Aihe.otsikko AS otsikko, "
                + "COUNT(Viesti.viesti_id) AS viesteja, MAX(Viesti.ajankohta) AS viimeisin "
                + "FROM Aihe LEFT JOIN Viesti ON Aihe.aihe_id=Viesti.aihe_id WHERE Aihe.alue_id = ? " 
                + "GROUP BY Aihe.aihe_id ORDER BY MAX(Viesti.ajankohta) DESC;");
        
        stmt.setObject(1, alue_id);
        ResultSet rs = stmt.executeQuery();
        List<Aihe> aiheet = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String otsikko = rs.getString("otsikko");
            int viesteja = rs.getInt("viesteja");
            String viimeisin = rs.getString("viimeisin");

            aiheet.add(new Aihe(id, otsikko, viesteja, viimeisin, alue_id));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aiheet;        
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
