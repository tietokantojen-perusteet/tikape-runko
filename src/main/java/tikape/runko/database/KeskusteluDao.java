
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tikape.runko.domain.Keskustelu;
import tikape.runko.database.Database;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {
    
    private Database database;
    
    public KeskusteluDao(Database database) {
        this.database = database;
    }
    
    public List<Keskustelu> getAihealueet() throws SQLException {
        List<Keskustelu> aihealueet = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT keskusteluid,Aihealue FROM Keskustelu");
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("keskusteluid");
            String aihealue = rs.getString("Aihealue");
            Keskustelu yksittainenAihealue = new Keskustelu(id,aihealue);
            aihealueet.add(yksittainenAihealue);
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        return aihealueet;
    }
    public List<Keskustelu> getOtsikot() throws SQLException {
        List<Keskustelu> otsikot = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskuste;");
        
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("KeskustelunID");
            String otsikko = rs.getString("Otsikko");
            String aihealue = rs.getString("Aihealue");
            Keskustelu keskustelu = new Keskustelu(id,otsikko,aihealue);
            otsikot.add(keskustelu);
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        return otsikot;
    }
    public List<Keskustelu> getKetjut(String aihealue) throws SQLException {
        List<Keskustelu> otsikot = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT keskusteluid,aihealue,otsikko as Keskustelu FROM keskustelu "
                + "WHERE aihealue ='" + aihealue + "';");
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("keskusteluid");
            String otsikko = rs.getString("otsikko");

            Keskustelu keskustelu = new Keskustelu(id,otsikko,aihealue);
            otsikot.add(keskustelu);
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        return otsikot;
    }
    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu ");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("KeskusteluID");
        String otsikko = rs.getString("otsikko");
        String aihealue = rs.getString("aihealue");
        

        Keskustelu k = new Keskustelu(id, otsikko, aihealue);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }
    
    @Override
    public List<Keskustelu> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu");

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("KeskusteluID");
            String otsikko = rs.getString("otsikko");
            String aihealue = rs.getString("aihealue");
            

            keskustelut.add(new Keskustelu(id, otsikko, aihealue));
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelut;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
    public void luoKeskustelu(Keskustelu keskustelu) throws SQLException {
        String sql = "INSERT INTO Keskustelu "
                + "(KeskusteluID, otsikko, aihealue) VALUES ("
                + (keskustelu.getId()) + ", "
                + s(keskustelu.getOtsikko()) + ", "
                + s(keskustelu.getAihealue()) + " );";
        
        database.update(sql);
        
    }
        
    private String s(String s) {
        return "'" + s + "'";
    }

}

