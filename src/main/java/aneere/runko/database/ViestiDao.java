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

        try (Connection connection = database.getConnection()) {
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
        }
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

    public void poistaViesti(Viesti viesti) {
        
        viestit.remove(viesti);
        String poistettava = "";
        poistettava = "DELETE FROM Viesti WHERE sisalto = '" + viesti + "'";

        try (Connection conn = database.getConnection()) {
            Statement st = conn.createStatement();

            st.executeUpdate(poistettava);

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
        PreparedStatement stmt = connection.prepareStatement("SELECT Viesti.ViestiID, Viesti.sisalto, Viesti"
                + ".kellonaika, Viesti.kayttaja, Kayttaja.tunnus, Viesti.keskustelu FROM Viesti, Kayttaja, Keskustelu"
                + " WHERE Keskustelu.KeskusteluID = Viesti.keskustelu"
                + " AND Kayttaja.ID = Viesti.kayttaja"
                + " AND Keskustelu.KeskusteluID = " + otsikkoid);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer ID = rs.getInt("ViestiID");
            Integer kayttajanID = rs.getInt("Kayttaja");
            String tunnus = rs.getString("tunnus");
            Integer keskustelu = rs.getInt("Keskustelu");
            Timestamp kellonaika = rs.getTimestamp("kellonaika");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(ID, kayttajanID, keskustelu, kellonaika, sisalto, tunnus));

        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
    public int getViestienmaaraKeskustelu(Integer keskusteluID) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(Viesti.ViestiID) AS maara FROM Viesti, Keskustelu"
                + " WHERE Keskustelu.KeskusteluID = Viesti.keskustelu "
                + " AND Keskustelu.KeskusteluID = " + keskusteluID);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        
        Integer maara = rs.getInt("maara");


        rs.close();
        stmt.close();
        connection.close();

        return maara;
        
    }
    
     public int getViestienmaaraAihealue(String aihealue) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(Viesti.ViestiID) AS maara FROM Viesti, Keskustelu"
                + " WHERE Keskustelu.KeskusteluID = Viesti.keskustelu "
                + " AND Keskustelu.aihealue = " + aihealue);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        
        Integer maara = rs.getInt("maara");


        rs.close();
        stmt.close();
        connection.close();

        return maara;     
    }
     
      public int getViestienmaaraKaikki() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(Viesti.ViestiID) AS maara FROM Viesti, Keskustelu"
                + " WHERE Keskustelu.KeskusteluID = Viesti.keskustelu");

        ResultSet rs = stmt.executeQuery();
        
        Integer maara = rs.getInt("maara");
        
        rs.close();
        stmt.close();
        connection.close();

        return maara;     
    }
     
     

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
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
        PreparedStatement stmt = connection.prepareStatement("SELECT Viesti.ViestiID, Viesti.sisalto, Viesti.kellonaika,"
                + " Viesti.kayttaja, Kayttaja.tunnus, Viesti.keskustelu FROM Viesti, Kayttaja "
                + " WHERE Kayttaja.ID = Viesti.kayttaja "
                + " ORDER BY kellonaika DESC LIMIT " + montako);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("ViestiID");
            Integer kayttajanID = rs.getInt("Kayttaja");
            Integer keskustelu = rs.getInt("Keskustelu");
            String tunnus = rs.getString("tunnus");
            Timestamp kellonaika = rs.getTimestamp("kellonaika");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(id, kayttajanID, keskustelu, kellonaika, sisalto, tunnus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
}
