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

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}
