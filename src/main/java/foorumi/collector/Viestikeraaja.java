package foorumi.collector;

import foorumi.Sisalto.Alue;
import foorumi.Sisalto.Viesti;
import foorumi.database.Collector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Viestikeraaja implements Collector<Viesti> {

    @Override
    public Viesti collect(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String aihe = rs.getString("aihe");
        String sisalto = rs.getString("sisältö");
        String aika = rs.getTimestamp("aika").toString();
        
        return new Viesti(id, aihe, sisalto, aika);
    }

}
