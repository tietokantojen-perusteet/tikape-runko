package foorumi.database;

import foorumi.Sisalto.Alue;
import java.sql.*;
import java.util.*;

public class AlueDao implements Dao<Alue, String> {

    private String tietokantaosoite;

    public AlueDao(String osoite) {

        this.tietokantaosoite = osoite;

    }

    @Override
    public Alue findOne(String key) throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String nimi = rs.getString("nimi");
        int id = rs.getInt("id");
        
        Alue a = new Alue(nimi, id);

        rs.close();
        stmt.close();
        conn.close();

        return a;

    }
    
    public void lisaa(String nimi) throws SQLException {
        
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO Alue (nimi) VALUES ('" + nimi + "');");
        stmt.close();
        conn.close();

    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = DriverManager.getConnection(tietokantaosoite);

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");
        ResultSet rs = stmt.executeQuery();

        List<Alue> alueet = new ArrayList<>();

        while (rs.next()) {
            String nimi = rs.getString("nimi");
            int id = rs.getInt("id");

            Alue a = new Alue(nimi, id);
            alueet.add(a);

        }
        rs.close();
        stmt.close();
        connection.close();

        return alueet;

    }
    
    

    @Override
    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
