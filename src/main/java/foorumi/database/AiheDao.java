package foorumi.database;

import foorumi.Sisalto.Aihe;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AiheDao implements Dao<Aihe, String> {

    private String tietokantaosoite;

    public AiheDao(Database d, String connection) {
        this.tietokantaosoite = connection;
    }

    @Override
    public Aihe findOne(String key) throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM Aihe WHERE nimi = " + key);

        String nimi = null;
        int alue_id;
        Aihe a = null;
        Integer id = null;
        while (rs.next()) {

            nimi = rs.getString("nimi");
            alue_id = rs.getInt("alue");
            id = rs.getInt("id");

            a = new Aihe(nimi, alue_id + "", id);
        }

        conn.close();
        return a;
    }

    public void lisaa(String nimi, String alue_id) throws SQLException {

        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO Aihe (nimi, alue_id) "
                + "VALUES ('" + nimi + "', " + alue_id + ");");
        stmt.close();
        conn.close();

    }

    @Override
    public List<Aihe> findAll() throws SQLException {
        Connection conn = DriverManager.getConnection(tietokantaosoite);

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM Aihe");

        List<Aihe> aiheet = new ArrayList<>();

        while (rs.next()) {
            String nimi = rs.getString("nimi");
            String alue = rs.getString("alue");
            int id = rs.getInt("id");

            Aihe a = new Aihe(nimi, alue, id);
            aiheet.add(a);
        }

        conn.close();

        return aiheet;
    }

    public List<Aihe> findWithAlue(String name) throws SQLException {
        List<Aihe> aiheet = new ArrayList<>();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Aihe Where alue = '" + name + "'");
        while (rs.next()) {
            aiheet.add(new Aihe(rs.getString("nimi"), name, rs.getInt("id")));
        }
        rs.close();
        stmt.close();
        conn.close();
        return aiheet;
    }

    public List<Aihe> findWithId(String id) throws SQLException {
        List<Aihe> aiheet = new ArrayList<>();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Aihe WHERE alue_id =  " + id);
        while (rs.next()) {
            aiheet.add(new Aihe(rs.getString("nimi"), rs.getString("Alue_id"), rs.getInt("id")));
        }
        rs.close();
        stmt.close();
        conn.close();
        return aiheet;
    }

    public List<Aihe> findWithId(String id, String sivu) throws SQLException {
        List<Aihe> aiheet = new ArrayList<>();
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        int raja1 = (Integer.parseInt(sivu) - 1) * 10;
        String a = Integer.toString(raja1);
        String rajoitus = " LIMIT " + 10 + " OFFSET " + a;

        ResultSet rs = stmt.executeQuery("SELECT * FROM Aihe WHERE alue_id =  " + id + rajoitus);
        while (rs.next()) {
            aiheet.add(new Aihe(rs.getString("nimi"), rs.getString("Alue_id"), rs.getInt("id")));
        }
        rs.close();
        stmt.close();
        conn.close();
        return aiheet;
    }

    @Override
    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
