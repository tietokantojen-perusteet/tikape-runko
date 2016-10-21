
package foorumi.collector;

import foorumi.Sisalto.Aihe;
import foorumi.Sisalto.Alue;
import foorumi.database.Collector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Aihekeraaja implements Collector<Aihe> {

    @Override
    public Aihe collect(ResultSet rs) throws SQLException {
        String nimi = rs.getString("nimi");
        String alue = rs.getString("alue");
        
        return new Aihe(nimi, alue, rs.getInt("id"));
    }
  
    
}
