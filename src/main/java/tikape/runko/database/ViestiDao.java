package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.*;
import tikape.runko.domain.Kayttaja;
import tikape.runko.domain.Keskustelu;
import tikape.runko.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private List<Viesti> lista;

    public ViestiDao(Database database) {
        this.database = database;
        this.lista = new ArrayList();
    }

    public void lisaaViesti(Viesti viesti) {
        lista.add(viesti);
        String lisattava = "";
        lisattava = "INSERT INTO Viesti VALUES (" + viesti.getId() + ", " + viesti.getKayttajaID()
                + ", " + viesti.getKeskusteluID() + ", \"" + viesti.getSisalto() + "\", "
                + viesti.getKellonaika() + ")";

        try (Connection conn = database.getConnection()) {
            Statement st = conn.createStatement();

            st.executeUpdate(lisattava);

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }

    }

    public List<Viesti> getLista() {
        return this.lista;
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
            Integer id = rs.getInt("ViestiId");
            Integer kayttajanid = rs.getInt("KayttajanID");
//            Integer kayttaja = rs.getInt("Kayttaja");
            Integer keskustelu = rs.getInt("Keskustelu");
            Timestamp kellonaika = rs.getTimestamp("kellonaika");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(id, kayttajanid, keskustelu, sisalto, kellonaika));

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

}
