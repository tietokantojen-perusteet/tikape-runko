
package foorumi.collector;

import foorumi.Sisalto.Alue;
import foorumi.database.Collector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Aluekeraaja implements Collector<Alue> {

    @Override
    public Alue collect(ResultSet rs) throws SQLException {
        String nimi = rs.getString("nimi");
        
        return new Alue(nimi, rs.getInt("id"));
    }
    
    
    
}
