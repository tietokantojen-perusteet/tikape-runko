package foorumi.database;

import foorumi.Sisalto.Aihe;
import foorumi.Sisalto.Viesti;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ViestiDao implements Dao<Viesti, Integer> {

    private String tietokantaosoite;

    public ViestiDao(Database d, String osoite) {
        this.tietokantaosoite = osoite;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
//        Connection conn = DriverManager.getConnection(tietokantaosoite);
//        Statement stmt = conn.createStatement();
//        stmt.setObject(1, key);
//
//        ResultSet rs = stmt.executeQuery();
//        boolean hasOne = rs.next();
//
//        if (!hasOne) {
//            return null;
//        }
//
//        int id = rs.getInt("id");
//        String aihe = rs.getString("aihe");
//        String sisalto = rs.getString("sisältö");
//        Timestamp aika = rs.getTimestamp("aika");
//
//        Viesti v = new Viesti(id, aihe, sisalto, aika);
//
//        rs.close();
//        stmt.close();
//        conn.close();
//
//        return v;
        return null;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        return null;
    }
//        Connection conn = DriverManager.getConnection(tietokantaosoite);
//
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery(("SELECT * FROM Viesti"));
//
//        List<Viesti> viestit = new ArrayList<>();
//
//        while (rs.next()) {
//            int id = rs.getInt("id");
//            String aihe = rs.getString("aihe");
//            String sisalto = rs.getString("sisältö");
//            Timestamp aika = rs.getTimestamp("aika");
//
//            Viesti v = new Viesti(id, aihe, sisalto, aika);
//            viestit.add(v);
//        }
//        rs.close();
//        stmt.close();
//        conn.close();
//
//        return viestit;
//    }

    public void lisaa(String sisalto, String aihe_id, String lahettaja) throws SQLException {
        java.util.Date date = new java.util.Date();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        if (lahettaja.length() == 0) {
            lahettaja = "Unindentified";
        }
        sisalto = sisalto + " t. " + lahettaja;
        stmt.execute("INSERT INTO Viesti (aihe_id, sisältö, aika) VALUES ('" + aihe_id + "', '" + sisalto
                + "', CURRENT_TIMESTAMP)");
        stmt.close();
        conn.close();

    }

    public String getAihe(Integer id) throws SQLException {

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT aihe_id FROM Viesti WHERE id =  " + id);
        return rs.getString("aihe_id");

    }

    public List<Viesti> findWithAihe(String aihe_id) throws SQLException {
        List<Viesti> viestit = new ArrayList<>();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Viesti WHERE aihe_id = '" + aihe_id + "'");
        while (rs.next()) {
            viestit.add(new Viesti(aihe_id, rs.getString("sisältö"), "22.21.2012"));
        }
        rs.close();
        stmt.close();
        conn.close();
        return viestit;
    }

    public String laskeViestitAlueelta(String alue) throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        String palauta = null;
        ResultSet rs = stmt.executeQuery("SELECT COUNT(viesti.id) AS viestit FROM Viesti, Alue, Aihe "
                + "WHERE Viesti.aihe_id = aihe.id AND Aihe.alue_id = alue.id "
                + "AND Alue.nimi = '" + alue + "';");
        while (rs.next()) {
            palauta = rs.getString("viestit");
        }

        return palauta;
    }

    public String laskeViestitAiheelta(String aihe) throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        String palauta = null;
        ResultSet rs = stmt.executeQuery("SELECT COUNT(viesti.id) AS viestit FROM Viesti, Aihe "
                + "WHERE Viesti.aihe_id = aihe.id AND Aihe.nimi = '" + aihe + "';");
        while (rs.next()) {
            palauta = rs.getString("viestit");
        }
        return palauta;
    }

    public List<String> findAikaViesteilleAiheesta(String aihe_id) throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        List<String> ajat = new ArrayList<>();
        String aika = null;
        ResultSet rs = stmt.executeQuery("SELECT viesti.aika FROM Viesti, Aihe"
                + " WHERE Aihe.id = '" + aihe_id + "' AND Viesti.aihe_id = Aihe.id"
                + " GROUP BY Viesti.aika ORDER BY Viesti.aika ASC");
        while (rs.next()) {
            aika = rs.getString("aika");
            ajat.add(aika);
        }
        return ajat;
    }

    public String findUusinAikaAlueelta(String alue) throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        List<String> ajat = new ArrayList<>();
        String aika = null;
        ResultSet rs = stmt.executeQuery("SELECT viesti.aika FROM Viesti, Aihe, Alue"
                + " WHERE Alue.nimi = '" + alue + "' AND Viesti.aihe_id = Aihe.id AND Aihe.alue_id = Alue.id  "
                + " GROUP BY Viesti.aika ORDER BY Viesti.aika DESC LIMIT 1;");
        while (rs.next()) {
            aika = rs.getString("aika");
            System.out.println(aika);
        }
        return aika;
    }

    public String findUusinAikaAiheelta(String aihe) throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        List<String> ajat = new ArrayList<>();
        String aika = null;
        ResultSet rs = stmt.executeQuery("SELECT viesti.aika FROM Viesti, Aihe"
                + " WHERE Viesti.aihe_id = Aihe.id AND Aihe.nimi = '" + aihe + "' "
                + " GROUP BY Viesti.aika ORDER BY Viesti.aika DESC LIMIT 1");
        while (rs.next()) {
            aika = rs.getString("aika");
            System.out.println(aika);
        }
        return aika;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
