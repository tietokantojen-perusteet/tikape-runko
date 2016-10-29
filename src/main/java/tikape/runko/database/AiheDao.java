/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aihe;
import tikape.runko.domain.Alue;

public class AiheDao implements Dao<Aihe, Integer> {

    private Database database;

    public AiheDao(Database database) {
        this.database = database;
    }

    @Override
    public Aihe findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer alue = rs.getInt("alue_id");
        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Aihe o = new Aihe(id, nimi, alue);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    public String findAlueid(String key) throws SQLException {
        Aihe aihe = findOne(Integer.parseInt(key));
        String alueid = "" + aihe.getAlue();
        return alueid;
    }

    public List<Aihe> findAlueesta(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe WHERE alue_id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        List<Aihe> aiheet = new ArrayList<>();
        while (rs.next()) {
            Integer alue_id = rs.getInt("alue_id");
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aiheet.add(new Aihe(id, nimi, alue_id));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aiheet;
    }

    @Override
    public List<Aihe> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe");

        ResultSet rs = stmt.executeQuery();
        List<Aihe> aiheet = new ArrayList<>();
        while (rs.next()) {
            AlueDao ad = new AlueDao(this.database);
            Integer alue_id = rs.getInt("alue_id");
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aiheet.add(new Aihe(id, nimi, alue_id));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aiheet;
    }

    // Aiheet top 10 EI TOIMI VIELÄ!!!
    public List<Aihe> findViimeisimmatKymmenenAihetta(Integer alueid) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT Aihe.alue_id, Aihe.id, Aihe.nimi, MAX(Viesti.time) AS time FROM Aihe LEFT JOIN Viesti ON Aihe.id = Viesti.aihe_id WHERE alue_id = ? GROUP BY Aihe.nimi ORDER BY time DESC LIMIT 10");

        stmt.setInt(1, alueid);

        ResultSet rs = stmt.executeQuery();

        List<Aihe> aiheet = new ArrayList<>();

        while (rs.next()) {
            Integer alue_id = rs.getInt("alue_id");
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aiheet.add(new Aihe(id, nimi, alue_id));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aiheet;
    }

    public void create(String alueid, String nimiSyote) throws SQLException {
        Connection connection = database.getConnection();

        String nimi = nimiSyote.trim(); //poistaa välilyönnit
        if (nimi.isEmpty()) { //varmistaa, ettei voi luoda nimetöntä aihetta
            return;
        }

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Aihe (alue_id, nimi) VALUES (?, ?)");
        stmt.setObject(1, alueid);
        stmt.setObject(2, nimi);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public Integer getLukumaara(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) AS viestit FROM Viesti, Aihe, Alue WHERE Viesti.aihe_id = Aihe.id AND Aihe.alue_id = Alue.id AND Aihe.id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        int palauta = rs.getInt("viestit");
        rs.close();
        stmt.close();
        connection.close();

        return palauta;
    }

    public String getViimeisin(Integer aiheid) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT Viesti.time FROM Viesti, Aihe, Alue WHERE Viesti.aihe_id = Aihe.id AND Aihe.alue_id = Alue.id AND Aihe.id = ? ORDER BY Viesti.time DESC LIMIT 1");

        stmt.setObject(1, aiheid);

        ResultSet rs = stmt.executeQuery();

        if (rs.isClosed()) {
            return "ei viesteja";
        }

        String palauta = rs.getString("time");

        rs.close();
        stmt.close();
        connection.close();

        return palauta;

    }

    public void poista(String id) throws Exception {
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        try {
            Integer.parseInt(id);
        } catch (Throwable t) {
            return;
        }

        statement.execute("DELETE FROM Viesti WHERE aihe_id = " + id);
        statement.execute("DELETE FROM Aihe WHERE id = " + id);
        connection.close();
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}
