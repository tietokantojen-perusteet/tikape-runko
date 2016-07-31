
package aneere.runko.database;

import java.sql.*;
import java.util.*;
import aneere.runko.domain.Keskustelu;
import aneere.runko.database.Database;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {
    
    private Database database;
    private List<Keskustelu> otsikot;
    
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
        
        this.otsikot = otsikot;
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
    public String getAihealue(int haettavaID) throws SQLException {
        List<Keskustelu> lista = getOtsikot();
        for (Keskustelu keskustelu : lista) {
            if(keskustelu.getID() == haettavaID) {
                return keskustelu.getAihealue();
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
                + "WHERE aihealue ='?';");
        stmt.setObject(1, aihealue);
        
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
    public boolean poistaKeskustelu(int poistettavaID) throws SQLException {
        boolean onnistuu = false;
        Keskustelu poistettava = null;
        
        for (Keskustelu keskustelu : otsikot) {
            if (keskustelu.getID() == poistettavaID) {
                poistettava = keskustelu;
                onnistuu = true;
                break;
            }
        }
        
        if (poistettava != null) {
            otsikot.remove(poistettava);
        }
        
        if (onnistuu == true) {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Keskustelu WHERE KeskusteluID = ?");
            stmt.setObject(1, poistettavaID);
            stmt .executeUpdate();
        }
        return onnistuu;
    }
    
    public void luoKeskustelu(Keskustelu keskustelu) throws SQLException {
        
//        String sql = "INSERT INTO Keskustelu VALUES("
//                + keskustelu.getID() + ", '"
//                + keskustelu.getOtsikko() + "', '"
//                + keskustelu.getAihealue() + "')";
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskustelu VALUES (?, ?, ?)");
               
        stmt.setObject(1, keskustelu.getID());
        stmt.setObject(2, keskustelu.getOtsikko());
        stmt.setObject(3, keskustelu.getAihealue());      
        
        stmt .executeUpdate();
        otsikot.add(keskustelu);
    }        

    public int getSeuraavaID() throws SQLException {
        int seuraavaID = 0;
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT KeskusteluID FROM Keskustelu ORDER BY KeskusteluID DESC LIMIT 1");
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                seuraavaID = rs.getInt("KeskusteluID");
            }            
            rs.close();
            stmt.close();
        }        
        
        return seuraavaID+1;
    }
}


