
package tikape.runko.database;

import java.sql.*;
import java.util.*;
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
        PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT Aihealue FROM Keskustelu");
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            //int id = rs.getInt("KeskusteluID");
            String aihealue = rs.getString("Aihealue");
            Keskustelu yksittainenAihealue = new Keskustelu(aihealue);
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

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu;");

//        PreparedStatement stmt = connection.prepareStatement("SELECT KeskusteluID,Otsikko,Aihealue FROM Keskustelu;");

        
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("KeskusteluID");
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
    public Keskustelu getOtsikko(String otsikko) throws SQLException {
        List<Keskustelu> lista = getOtsikot();
        for (Keskustelu keskustelu : lista) {
            if (keskustelu.getOtsikko().equals(otsikko)) {
                return keskustelu;
            }
        }
        return null;
    }
    public int getOtsikkoID(String otsikko) throws SQLException {
        List<Keskustelu> lista = getOtsikot();
        for (Keskustelu keskustelu : lista) {
            if (keskustelu.getOtsikko().equals(otsikko)) {
                return keskustelu.getID();
            }
        }
        return 0;
    }
    public List<Keskustelu> getKetjut(String aihealue) throws SQLException {
        List<Keskustelu> otsikot = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT keskustelu,aihealue,otsikko as Keskustelu FROM keskustelu "
                + "WHERE aihealue ='" + aihealue + "';");
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("keskustelu");
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
        String otsikko = rs.getString("Otsikko");
        String aihealue = rs.getString("Aihealue");
        

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
            String otsikko = rs.getString("Otsikko");
            String aihealue = rs.getString("Aihealue");
            

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
                + (keskustelu.getID()) + ", "
                + s(keskustelu.getOtsikko()) + ", "
                + s(keskustelu.getAihealue()) + " );";
        
        database.update(sql);
        
    }
        
    private String s(String s) {
        return "'" + s + "'";
    }

}

