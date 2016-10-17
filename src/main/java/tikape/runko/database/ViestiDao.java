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
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Keskustelu;
import tikape.runko.domain.Viesti;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author tamella
 */
// tämä luokka koostuu niistä kyselyistä, jotka taululle Viesti voidaan esittää
public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private KeskusteluDao keskusdao;

    public ViestiDao(Database base, KeskusteluDao keskusdao) {
        this.database = base;
        this.keskusdao = keskusdao;
    }

    // ensin perus daotoiminnalisuus, eli etsi yksittäinen ja etsi kaikki
    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE viesti_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("viesti_id");
//        Date paivamaara = rs.getDate("julkaisuaika");
        Timestamp paivamaara = rs.getTimestamp("julkaisuaika");
        String kirjoittaja = rs.getString("kirjoittaja");
        String teksti = rs.getString("teksti");

        Viesti v = new Viesti(id, paivamaara, kirjoittaja, teksti);

        Integer omakeskustelu = rs.getInt("omakeskustelu");

        v.setOmakeskustelu(this.keskusdao.findOne(omakeskustelu));

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("viesti_id");
//            Date paivamaara = rs.getDate("julkaisuaika");
            Timestamp paivamaara = rs.getTimestamp("julkaisuaika");
            String kirjoittaja = rs.getString("kirjoittaja");
            String teksti = rs.getString("teksti");

            Integer omakes = rs.getInt("omakeskustelu");

            Viesti tekstiteksti = new Viesti(id, paivamaara, kirjoittaja, teksti);
            tekstiteksti.setOmakeskustelu(this.keskusdao.findOne(omakes));
            viestit.add(tekstiteksti);
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    public List<Viesti> viestitJarjestys(Integer keskusteluid) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE omakeskustelu = ?");
        stmt.setObject(1, keskusteluid);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        
        while (rs.next()) {
            
            Integer id = rs.getInt("viesti_id");
            Timestamp aika = rs.getTimestamp("julkaisuaika");
            String kirjoittaja = rs.getString("kirjoittaja");
            String teksti = rs.getString("teksti");

            Keskustelu omakes = keskusdao.findOne(keskusteluid);

            Viesti viesti = new Viesti(id, aika, kirjoittaja, teksti);

            viesti.setOmakeskustelu(omakes);
            viestit.add(viesti);
        }
        
        Collections.sort(viestit);
        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    // antaa keskustelut järjestettynä viimeisimmän viestin mukaan
    public List<Keskustelu> keskustelutJarjestys(Integer alueid) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE omaalue = ?");
        stmt.setObject(1, alueid);

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();

        while (rs.next()) {
            
            Integer id = rs.getInt("keskustelu_id");
            String otsikko = rs.getString("otsikko");
            String aloittaja = rs.getString("aloittaja");
            String teksti = rs.getString("aloitusviesti");
            Timestamp paivamaara = rs.getTimestamp("paivamaara");

            Integer omaalue = rs.getInt("omaalue");

            Keskustelu palapala = new Keskustelu(id, otsikko, aloittaja, teksti, paivamaara);
            AlueDao aluedao = keskusdao.getAlueDao();
            palapala.setOmaalue(aluedao.findOne(omaalue));
            keskustelut.add(palapala);
        }
        Collections.sort(keskustelut, new Comparator<Keskustelu>() {
            public int compare(Keskustelu k1, Keskustelu k2) {
                try {
                    return keskustelunUusin(k1.getId()).compareTo(keskustelunUusin(k2.getId()));
                } catch (Exception e) {
                    System.out.println("");
                }
                return 1;
            }
        });
        rs.close();
        stmt.close();
        connection.close();

        return keskustelut;
    }

    // antaa alueet järjestettynä viimeisimmän viestin mukaan
    public List<Alue> alueetJarjestys() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("alue_id");
            String nimi = rs.getString("nimi");

            Alue alue = new Alue(id, nimi);

            alueet.add(alue);
        }
        Collections.sort(alueet, new Comparator<Alue>() {
            public int compare(Alue a1, Alue a2) {
                try {
                    return a1.getNimi().compareTo(a2.getNimi());
                } catch (Exception e) {
                    System.out.println("");
                }
                return 1;
            }
        });
        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    public int keskustelunViestienmaara(Integer keskustelu_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) AS luku FROM Viesti WHERE viesti.omakeskustelu = ?");
        stmt.setObject(1, keskustelu_id);

        ResultSet rs = stmt.executeQuery();
        int vastaus = rs.getInt("luku") + 1;
        // lisätty avausviesti +1 /VK
        rs.close();
        stmt.close();
        connection.close();

        return vastaus;
    }

    public int alueenViestienmaara(Integer alueid) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE Keskustelu.omaalue = ?");
        stmt.setObject(1, alueid);

        ResultSet rs = stmt.executeQuery();
        int summa = 0;
        while (rs.next()) {
            summa = summa + keskustelunViestienmaara(rs.getInt("keskustelu_id"));
        }

        rs.close();
        stmt.close();
        connection.close();

        return summa;
    }

    // antaa uusimman viestin päiväyksen
    public Timestamp keskustelunUusin(Integer keskusteluid) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE Viesti.omakeskustelu = ? ");

        stmt.setObject(1, keskusteluid);

        Timestamp aloitus = keskusdao.findOne(keskusteluid).getTime();

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return aloitus;
        }
        Timestamp yrite = rs.getTimestamp("julkaisuaika");

        while (rs.next()) {
            Timestamp haastaja = rs.getTimestamp("julkaisuaika");
            if (yrite.before(haastaja)) {
                yrite = haastaja;
            }
        }

        rs.close();
        stmt.close();
        connection.close();

        return yrite;

    }

    // antaa uusimman viestin päiväyksen
    public Timestamp alueenUusin(Integer alueid) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE Keskustelu.omaalue = ? ");

        stmt.setObject(1, alueid);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        Timestamp yrite = keskustelunUusin(rs.getInt("keskustelu_id"));

        while (rs.next()) {
            Timestamp haastaja = keskustelunUusin(rs.getInt("keskustelu_id"));
            if (yrite.before(haastaja)) {
                yrite = haastaja;
            }
        }

        rs.close();
        stmt.close();
        connection.close();

        return yrite;
    }

    public void lisaaViesti(Integer keskusteluid, String kirjoittaja, String teksti) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Viesti (omakeskustelu, kirjoittaja, teksti)VALUES (?, ?, ?)");
        stmt.setObject(1, keskusteluid);
        stmt.setObject(2, kirjoittaja);
        stmt.setObject(3, teksti);
        stmt.execute();

        conn.close();
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}
