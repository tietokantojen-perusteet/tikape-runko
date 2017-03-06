package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.domain.Ketju;

/**
 *
 * @author aleksimu
 */
public class KetjuDao implements Dao<Ketju, Integer> {

    private Database database;

    public KetjuDao(Database database) {
        this.database = database;
    }

    @Override
    public Ketju findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju WHERE ketju = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer ketju = rs.getInt("ketju");
        String aihe = rs.getString("aihe");
        String otsikko = rs.getString("otsikko");
        String sisalto = rs.getString("sisalto");
        String aloitusaika = rs.getString("aloitusaika");
        String kayttajanimi = rs.getString("kayttajanimi");

        Ketju k = new Ketju(ketju, aihe, otsikko, sisalto, aloitusaika, kayttajanimi);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Ketju> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju ORDER BY aloitusaika ASC");

        ResultSet rs = stmt.executeQuery();
        List<Ketju> ketjut = new ArrayList<>();
        while (rs.next()) {

            Integer ketju = rs.getInt("ketju");
            String aihe = rs.getString("aihe");
            String otsikko = rs.getString("otsikko");
            String sisalto = rs.getString("sisalto");
            String aloitusaika = rs.getString("aloitusaika");
            String kayttajanimi = rs.getString("kayttajanimi");

            ketjut.add(new Ketju(ketju, aihe, otsikko, sisalto, aloitusaika, kayttajanimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ketjut;
    }

    public ArrayList<Ketju> findByAihealue(String alue) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju WHERE aihe = ? ORDER BY aloitusaika DESC");
        stmt.setString(1, alue);

        ResultSet rs = stmt.executeQuery();

//        if (!rs.next()) {
//            return null;
//        }
        ArrayList<Ketju> ketjut = new ArrayList<>();
        while (rs.next()) {

            Integer ketju = rs.getInt("ketju");
            String aihe = rs.getString("aihe");
            String otsikko = rs.getString("otsikko");
            String sisalto = rs.getString("sisalto");
            String aloitusaika = rs.getString("aloitusaika");
            String kayttajanimi = rs.getString("kayttajanimi");

            ketjut.add(new Ketju(ketju, aihe, otsikko, sisalto, aloitusaika, kayttajanimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ketjut;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Ketju WHERE ketju = ?");
        stmt.setInt(1, key);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public Integer viestienMaara(int key) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM Ketju, Viesti WHERE Ketju.ketju = ? AND Viesti.ketju = Ketju.ketju");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        Integer pal = Integer.parseInt(rs.getString(1)) + 1;

        rs.close();
        stmt.close();
        connection.close();

        return pal;
    }

    //Saattaa olla turha tämä
    public String getAloitusaika(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ketju WHERE ketju = ? ");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        String aloitusaika = rs.getString("aloitusaika");

        rs.close();
        stmt.close();
        connection.close();

        return aloitusaika;
    }

    public String getViimeisin(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        if (viestienMaara(key) <= 1) {
            return getAloitusaika(key);
        } else {
            PreparedStatement stmt = connection.prepareStatement("SELECT aika FROM Viesti WHERE ketju = ? ORDER BY aika DESC LIMIT 1");
            stmt.setInt(1, key);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            String aika = rs.getString(1);

            rs.close();
            stmt.close();
            connection.close();

            return aika;
        }

    }

    public void add(Ketju p) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Ketju (ketju, aihe, otsikko, sisalto, kayttajanimi) VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, p.getKetju());
        stmt.setString(2, p.getAihe());
        stmt.setString(3, p.getOtsikko());
        stmt.setString(4, p.getSisalto());
        stmt.setString(5, p.getKayttajanimi());
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
