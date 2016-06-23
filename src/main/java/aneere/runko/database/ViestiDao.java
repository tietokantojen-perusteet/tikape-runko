package aneere.runko.database;

import java.sql.*;
import java.util.*;
import aneere.runko.domain.Kayttaja;
import aneere.runko.domain.Keskustelu;
import aneere.runko.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private List<Viesti> viestit;

    public ViestiDao(Database database) throws SQLException {
        this.database = database;
        this.viestit = new ArrayList();
        init();
    }
    public void init() throws SQLException {
        
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Integer ID = rs.getInt("ViestiID");
            Integer kayttajanID = rs.getInt("Kayttaja");
            Integer keskusteluID = rs.getInt("Keskustelu");
            Timestamp kellonaika = rs.getTimestamp("kellonaika");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(ID, kayttajanID, keskusteluID, kellonaika, sisalto));
        }

        rs.close();
        stmt.close();
        connection.close();
    }

    public void lisaaViesti(Viesti viesti) {
        viestit.add(viesti);
        String lisattava = "";
        lisattava = "INSERT INTO Viesti VALUES (" + viesti.getID() + ", " + viesti.getKayttajaID()
                + ", " + viesti.getKeskusteluID() + ", '" + viesti.getKellonaika() + "', '"
                + viesti.getSisalto() + "')";

        try (Connection conn = database.getConnection()) {
            Statement st = conn.createStatement();

            st.executeUpdate(lisattava);

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }

    }

    public List<Viesti> getLista() {
        return this.viestit;
    }
    public int getSeuraavaID() {
        return this.viestit.size();
    }
//    @Override
//    public Viesti findOne(Integer key) throws SQLException {
//        Connection connection = database.getConnection();
//        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti "
//                + "ORDER BY datetime DESC LIMIT 1");
//        stmt.setObject(1, key);
//
//        ResultSet rs = stmt.executeQuery();
//        boolean hasOne = rs.next();
//        if (!hasOne) {
//            return null;
//        }
//
//        int id = rs.getInt("ViestiId");
//        Integer kayttajanid = rs.getInt("KayttajanID");
//        String kayttaja = rs.getString("Kayttaja");
//        Integer keskustelu = rs.getInt("Keskustelu");
//
//        java.util.Date kellonaika = rs.getDate("kellonaika");
//        String sisalto = rs.getString("sisalto");
//
//        Viesti v = new Viesti(id, kayttaja, keskustelu, sisalto, kellonaika);
//
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return k;
//    }
    public List<Viesti> getKetjuviestit(Integer otsikkoid) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti INNER JOIN"
                + " Keskustelu ON Keskustelu.KeskusteluID = Viesti.keskustelu "
                + " WHERE Keskustelu.KeskusteluID = " + otsikkoid);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer ID = rs.getInt("ViestiId");
            Integer kayttajanID = rs.getInt("Kayttaja");
            Integer keskustelu = rs.getInt("Keskustelu");
            Timestamp kellonaika = rs.getTimestamp("kellonaika");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(ID, kayttajanID, keskustelu, kellonaika, sisalto));

        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<String> getForeignKeys(String tableName) throws SQLException {

        ArrayList<String> foreignKeys = new ArrayList<String>();
        Connection connection = database.getConnection();

        try {
            DatabaseMetaData meta = connection.getMetaData();

            ResultSet rs = meta.getExportedKeys(connection.getCatalog(), null, tableName);

            while (rs.next()) {
                String columnName = rs.getString(tableName);
                foreignKeys.add(columnName);
            }

            return foreignKeys;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Viesti> getUusimmat(int montako) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti "
                + "ORDER BY kellonaika DESC LIMIT " + montako);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer ID = rs.getInt("ViestiID");
            Integer kayttajanID = rs.getInt("Kayttaja");
            Integer keskustelu = rs.getInt("Keskustelu");
            Timestamp kellonaika = rs.getTimestamp("kellonaika");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(ID, kayttajanID, keskustelu, kellonaika, sisalto));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
}
