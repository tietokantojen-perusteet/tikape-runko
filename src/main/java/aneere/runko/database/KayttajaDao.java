package aneere.runko.database;

import java.sql.*;
import java.util.*;
import aneere.runko.domain.Kayttaja;
import aneere.runko.database.Database;

public class KayttajaDao implements Dao<Kayttaja, Integer> {

    private Database database;
    private List<Kayttaja> kayttajat;

    public KayttajaDao(Database database) throws SQLException {
        this.database = database;
        this.kayttajat = findAll();
    }

    @Override
    public Kayttaja findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String tunnus = rs.getString("tunnus");
        String salasana = rs.getString("salasana");
        String email = rs.getString("email");

        Kayttaja k = new Kayttaja(id, tunnus, salasana, email);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Kayttaja> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja");

        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("tunnus");
            String salasana = rs.getString("salasana");
            String email = rs.getString("email");

            kayttajat.add(new Kayttaja(id, nimi, salasana, email));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kayttajat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    public boolean kirjaudu(Kayttaja kayttaja) {
        if (kayttajat.contains(kayttaja)) {
            return true;
        }
        return false;
    }

    public Kayttaja getKayttaja(String tunnus) {
        for (Kayttaja kayttaja : kayttajat) {
            if (kayttaja.getTunnus().equals(tunnus)) {
                return kayttaja;
            }
        }
        return null;
    }

    public int getSeuraavaID() {
        return kayttajat.size() + 1;
    }

    public void luoKayttaja(Kayttaja kayttaja) throws SQLException {
//        String sql = "INSERT INTO Kayttaja "
//                + "(Id, tunnus, salasana, email) VALUES ("
//                + (kayttaja.getID()) + ", "
//                + s(kayttaja.getTunnus()) + ", "
//                + s(kayttaja.getSalasana()) + ", "
//                + s(kayttaja.getEmail()) + " );";

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Kayttaja"
                + "(Id, tunnus, salasana, email) VALUES (?, ?, ?, ?)");
               
        stmt.setObject(1, kayttaja.getID());
        stmt.setObject(2, kayttaja.getTunnus());
        stmt.setObject(3, kayttaja.getSalasana());
        stmt.setObject(4, kayttaja.getEmail());

        stmt .executeUpdate();
        //database.update(stmt);
        kayttajat.add(kayttaja);
    }

    public String haeViestit(Kayttaja kayttaja) throws SQLException {
        String nakyma = "";
        int ID = kayttaja.getID();
        Connection connection = database.getConnection();
        //PreparedStatement stmt = connection.prepareStatement("SELECT sisalto, kellonaika FROM Viesti WHERE Viesti.kayttaja = ?" + kayttaja.getID());
        PreparedStatement stmt = connection.prepareStatement("SELECT sisalto, kellonaika FROM Viesti WHERE Viesti.kayttaja = ?");
        stmt.setObject(1, ID);
        List<String> lista = new ArrayList();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            nakyma += rs.getString("sisalto") + " " +  rs.getTimestamp("kellonaika") + "<br>";
            lista.add(nakyma);
        }
        
        int luku = lista.size();
        String lukustring = "" + luku;

        rs.close();
        stmt.close();
        connection.close();

        return nakyma + " viestejä yhteensä: " + lukustring;

    }

    private String s(String s) {
        return "'" + s + "'";
    }

}
