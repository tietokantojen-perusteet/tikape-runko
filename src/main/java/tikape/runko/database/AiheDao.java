package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static tikape.runko.database.AlueDao.SARAKEALUEID;
import tikape.runko.domain.Aihe;

public class AiheDao implements Dao<Aihe, Integer> {

    private Database database;
    protected static final String SARAKEAIHEID = "aihe_id";

    public AiheDao(Database database) {
        this.database = database;
    }

    // Luodaan uusi aihe tietokantaan. Tietokanta luo aiheelle aihe_id:n
    public Aihe create(Aihe uusiAihe) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Aihe (" + SARAKEALUEID + ", otsikko) "
                + "VALUES ( ? , ? )");
        stmt.setObject(1, uusiAihe.getAlue_id());
        stmt.setObject(2, uusiAihe.getOtsikko());
        stmt.execute();

        // etsitään luodun uuden aiheen aihe_id
        stmt = connection.prepareStatement("SELECT " + SARAKEAIHEID +" FROM Aihe "
                + "WHERE " + SARAKEALUEID + " = ? AND otsikko = ? "
                + "ORDER BY " + SARAKEAIHEID + " DESC;");
        stmt.setObject(1, uusiAihe.getAlue_id());
        stmt.setObject(2, uusiAihe.getOtsikko());
        ResultSet rs = stmt.executeQuery();

        // jos luonti epäonnistui palautuu null
        if (!rs.next()) {
            return null;
        }

        // palauetaan Aihe oli, joka sisältää tietokannan tekemän aihe_id:n
        int id = rs.getInt(SARAKEAIHEID);
        Aihe luotuAihe = new Aihe(id, uusiAihe.getOtsikko(), 0, "", uusiAihe.getAlue_id());
        stmt.close();
        connection.close();
        return luotuAihe;
    }

    // etsitään aihe_id:n perusteella yksi Aihe sekä siihen liittyvien viestien määrä ja viimeisin ajankohta
    @Override
    public Aihe findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        // luodaan erilasiet hauit sqlite ja postgresql varten jotta kellonaika toimii järkevästi
        String viimeisinAika = "DATETIME(MAX(Viesti.ajankohta),"
                + " 'localtime')  AS viimeisin, ";
        if (database.getDatabaseAddress().contains("postgres")) {
            viimeisinAika = "TO_CHAR(MAX(Viesti.ajankohta) "
                    + "AT TIME ZONE 'UTC' AT TIME ZONE 'EEST',"
                    + " 'YYYY-MM-DD HH24:MI:SS' ) AS viimeisin, ";
        }
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT Aihe.aihe_id AS id, Aihe.otsikko AS otsikko, "
                + "COUNT(Viesti.viesti_id) AS viesteja, "
                + viimeisinAika
                + "Aihe.alue_id AS alue_id "
                + "FROM Aihe LEFT JOIN Viesti "
                + "ON Aihe.aihe_id=Viesti.aihe_id "
                + "WHERE Aihe.aihe_id = ? "
                + "GROUP BY Aihe.aihe_id "
                + "ORDER BY MAX(Viesti.ajankohta) DESC;");

        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();

        // aihe_id:n mukaista aihetta ei ollut tietokannassa , palautetaan null
        if (!rs.next()) {
            return null;
        }

        // palauetaan löydetty Aihe
        int id = rs.getInt("id");
        String otsikko = rs.getString("otsikko");
        int viesteja = rs.getInt("viesteja");
        String viimeisin = rs.getString("viimeisin");
        int alue_id = rs.getInt("alue_id");

        Aihe aihe = new Aihe(id, otsikko, viesteja, viimeisin, alue_id);

        rs.close();
        stmt.close();
        connection.close();

        return aihe;
    }

    // tätä ei ole valmiina, koska sitä ei tarvittu projektin alkuvaiheessa, vaiko TODO
    @Override
    public List<Aihe> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // etsitään alue_id:n perusteella yhden alueen kaikki aiheet sekä viestin määrä niissä ja viimeisen visestin ajankohta
    @Override
    public List<Aihe> findAllIn(Integer id) throws SQLException {
        Connection connection = database.getConnection();
        // generoidaan sqliteä ja postgreSQL varten omat hauit, jotta kellon aika menee oikein molemmissa
        String viimeisinAika = "DATETIME(MAX(Viesti.ajankohta), 'localtime')  AS viimeisin ";
        if (database.getDatabaseAddress().contains("postgres")) {
            viimeisinAika = "TO_CHAR(MAX(Viesti.ajankohta) AT TIME ZONE 'UTC' AT TIME ZONE 'EEST', 'YYYY-MM-DD HH24:MI:SS' ) AS viimeisin ";
        }
        PreparedStatement stmt = connection.prepareStatement("SELECT Aihe.aihe_id AS id, Aihe.otsikko AS otsikko, "
                + "COUNT(Viesti.viesti_id) AS viesteja, "
                + viimeisinAika
                + "FROM Aihe LEFT JOIN Viesti ON Aihe.aihe_id=Viesti.aihe_id WHERE Aihe.alue_id = ? "
                + "GROUP BY Aihe.aihe_id ORDER BY MAX(Viesti.ajankohta) DESC;");

        stmt.setObject(1, id);
        ResultSet rs = stmt.executeQuery();

        List<Aihe> aiheet = new ArrayList<>();
        while (rs.next()) {
            int aihe_id = rs.getInt("id");
            String otsikko = rs.getString("otsikko");
            int viesteja = rs.getInt("viesteja");
            String viimeisin = rs.getString("viimeisin");

            aiheet.add(new Aihe(aihe_id, otsikko, viesteja, viimeisin, id));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aiheet;
    }

    // tekemättä koska ei tarvita projektissa, vaiko TODO
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
