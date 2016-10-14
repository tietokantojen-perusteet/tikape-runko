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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        return (Alue) database.queryAndCollect("SELECT * FROM Alue WHERE id = ?",
                rs -> new Alue(rs.getInt("id"), rs.getString("nimi")), key).get(0);
    }

//    @Override
//    public List<Alue> findAll() throws SQLException {
//
//        Connection connection = database.getConnection();
//        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");
//
//        ResultSet rs = stmt.executeQuery();
//        List<Alue> alueet = new ArrayList<>();
//        while (rs.next()) {
//            Integer id = rs.getInt("id");
//            String nimi = rs.getString("nimi");
//
//            alueet.add(new Alue(id, nimi));
//        }
//
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return alueet;
//    }
    public List<Alue> findEtusivunAlueet() throws SQLException {
//        return database.queryAndCollect(
//                "SELECT Alue.*, COUNT(Viesti.id) AS viestienLkm, MAX(Viesti.aika) AS viimeisinAika "
//                + "FROM Alue LEFT JOIN Keskustelu ON Alue.id = Keskustelu.alue_id "
//                + "LEFT JOIN Viesti ON Keskustelu.id = Viesti.keskustelu_id "
//                + "GROUP BY Alue.id",
//                rs -> new Alue(rs.getInt("id"), rs.getString("nimi"), rs.getInt("viestienLkm"), rs.getTimestamp("viimeisinAika")));
        // Ajan palautus getTimestamp-metodin haluamassa muodossa:
        return database.queryAndCollect(
                "SELECT Alue.*, COUNT(Viesti.id) AS viestienLkm, MAX(Viesti.aika) AS viimeisinAika "
                + "FROM Alue LEFT JOIN Keskustelu ON Alue.id = Keskustelu.alue_id "
                + "LEFT JOIN Viesti ON Keskustelu.id = Viesti.keskustelu_id "
                + "GROUP BY Alue.id ORDER BY Alue.nimi",
                rs -> new Alue(rs.getInt("id"), rs.getString("nimi"), rs.getInt("viestienLkm"), rs.getTimestamp("viimeisinAika")));
    }

//    public void delete(Integer key) throws SQLException {
//        // ei toteutettu
//    }
}
