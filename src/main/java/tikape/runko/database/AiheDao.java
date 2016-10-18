package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.domain.*;


public class AiheDao implements Dao<Aihe, Integer> {
    private Database database;
    private Dao<Alue, Integer> alueDao;
    
    public AiheDao(Database database, Dao<Alue, Integer> alueDao) {
        this.database = database;
        this.alueDao = alueDao;
    }
    
    @Override
    public Aihe create(Aihe a) throws SQLException {
        Connection conn = database.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO Aihe (alue, aloittaja, sisalto, otsikko, luotu) "
          + "VALUES (?, ?, ?, ?, ?);", 
            Statement.RETURN_GENERATED_KEYS);
        
        stmt.setInt(1, a.getAlue().getTunnus());
        stmt.setString(2, a.getAloittaja());
        stmt.setString(3, a.getSisalto());
        stmt.setString(4, a.getOtsikko());
        stmt.setTimestamp(5, a.getLuotu());
        
        stmt.executeUpdate();
        
        // Haetaan aiheelle annettu avain ja asetetaan se olion tunnukseksi
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        a.setTunnus(rs.getInt(1));
        
        rs.close();
        stmt.close();
        conn.close();
        
        return a;
    }

    @Override
    public Aihe findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT a.*, MAX(lahetetty) as uusin_viesti, COUNT(*) as viestimaara FROM Aihe a"
	  + "INNER JOIN Viesti v ON a.tunnus = v.aihe"
	  + "WHERE a.tunnus = ?;"
        );
        
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        
        if (!hasOne) {
            return null;
        }

        Integer alueTunnus = rs.getInt("alue");
        String aloittaja = rs.getString("aloittaja");
        String sisalto = rs.getString("sisalto");
        String otsikko = rs.getString("otsikko");
        Timestamp luotu = rs.getTimestamp("luotu");
        Timestamp viimViesti = rs.getTimestamp("viimeisin_viesti");
        Integer viestimaara = rs.getInt("viestimaara");

        Aihe aihe = new Aihe(key, aloittaja, sisalto, otsikko, 
                             luotu, viimViesti, viestimaara);
        aihe.setAlue(alueDao.findOne(alueTunnus));

        rs.close();
        stmt.close();
        connection.close();

        return aihe;
    }

    @Override
    public List<Aihe> findAll() throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT a.*, MAX(lahetetty) as uusin_viesti, COUNT(*) as viestimaara FROM Aihe a"
	  + "INNER JOIN Viesti v ON a.tunnus = v.aihe"
	  + "GROUP BY a.tunnus;"
        );
        
        ResultSet rs = stmt.executeQuery();
        
        Map<Integer, List<Aihe>> alueidenAiheet = new HashMap<>();
        
        List<Aihe> aiheet = new ArrayList<>();
        
        while (rs.next()) {
            Integer tunnus = rs.getInt("tunnus");
            Integer alueTunnus = rs.getInt("alue");
            String aloittaja = rs.getString("aloittaja");
            String sisalto = rs.getString("sisalto");
            String otsikko = rs.getString("otsikko");
            Timestamp luotu = rs.getTimestamp("luotu");
            Timestamp viimViesti = rs.getTimestamp("viimeisin_viesti");
            Integer viestimaara = rs.getInt("viestimaara");

            Aihe aihe = new Aihe(tunnus, aloittaja, sisalto, otsikko, 
                                 luotu, viimViesti, viestimaara);
            aiheet.add(aihe);
            
            if (!alueidenAiheet.containsKey(alueTunnus)) {
                alueidenAiheet.put(alueTunnus, new ArrayList<>());
            }
            
            alueidenAiheet.get(alueTunnus).add(aihe);
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        for (Alue alue : this.alueDao.findAllIn(alueidenAiheet.keySet())) {
            for (Aihe aihe : alueidenAiheet.get(alue.getTunnus())) {
                aihe.setAlue(alue);
            }
        }

        return aiheet;
    }

    @Override
    public void update(Integer key, Aihe t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Aihe> findAllIn(Collection<Integer> keys) throws SQLException {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        // Luodaan IN-kyselyä varten paikat, joihin arvot asetetaan
        StringBuilder arvot = new StringBuilder("?");
        for (int i = 1; i < keys.size(); i++) {
            arvot.append(", ?");
        }

        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT a.*, MAX(lahetetty) as uusin_viesti, COUNT(*) as viestimaara FROM Aihe a"
	  + "INNER JOIN Viesti v ON a.tunnus = v.aihe"
	  + "WHERE a.tunnus IN  = (" + arvot + ") GROUP BY a.tunnus;"
        );

        int laskuri = 1;
        
        for (Integer key : keys) {
            stmt.setInt(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        
        Map<Integer, List<Aihe>> alueidenAiheet = new HashMap<>();
        
        List<Aihe> aiheet = new ArrayList<>();
        
        while (rs.next()) {
            Integer tunnus = rs.getInt("tunnus");
            Integer alueTunnus = rs.getInt("alue");
            String aloittaja = rs.getString("aloittaja");
            String sisalto = rs.getString("sisalto");
            String otsikko = rs.getString("otsikko");
            Timestamp luotu = rs.getTimestamp("luotu");
            Timestamp viimViesti = rs.getTimestamp("viimeisin_viesti");
            Integer viestimaara = rs.getInt("viestimaara");

            Aihe aihe = new Aihe(tunnus, aloittaja, sisalto, otsikko, 
                                 luotu, viimViesti, viestimaara);
            aiheet.add(aihe);
            
            if (!alueidenAiheet.containsKey(alueTunnus)) {
                alueidenAiheet.put(alueTunnus, new ArrayList<>());
            }
            
            alueidenAiheet.get(alueTunnus).add(aihe);
        }
        
        rs.close();
        stmt.close();
        connection.close();
        
        for (Alue alue : this.alueDao.findAllIn(alueidenAiheet.keySet())) {
            for (Aihe aihe : alueidenAiheet.get(alue.getTunnus())) {
                aihe.setAlue(alue);
            }
        }

        return aiheet;
    }
    
    public List<Aihe> findAllInAlue(Alue alue) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT a.*, MAX(lahetetty) as uusin_viesti, COUNT(*) as viestimaara FROM Aihe a"
	  + "INNER JOIN Viesti v ON a.tunnus = v.aihe"
	  + "WHERE a.alue = ? GROUP BY a.tunnus;"
        );
        
        stmt.setInt(1, alue.getTunnus());
        
        ResultSet rs = stmt.executeQuery();
        
        List<Aihe> aiheet = new ArrayList<>();
        
        while (rs.next()) {
            Integer tunnus = rs.getInt("tunnus");
            String aloittaja = rs.getString("aloittaja");
            String sisalto = rs.getString("sisalto");
            String otsikko = rs.getString("otsikko");
            Timestamp luotu = rs.getTimestamp("luotu");
            Timestamp viimViesti = rs.getTimestamp("viimeisin_viesti");
            Integer viestimaara = rs.getInt("viestimaara");

            Aihe aihe = new Aihe(tunnus, aloittaja, sisalto, otsikko, 
                                 luotu, viimViesti, viestimaara);
            aiheet.add(aihe);
            
            aihe.setAlue(alue);
        }
        
        rs.close();
        stmt.close();
        connection.close();

        return aiheet;
    }
    
    public List<Aihe> findAllInAluePerPage(Alue alue, Integer pageNum) throws SQLException {
        Connection connection = database.getConnection();
         
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT a.*, MAX(lahetetty) as uusin_viesti, COUNT(*) as viestimaara FROM Aihe a"
	  + "INNER JOIN Viesti v ON a.tunnus = v.aihe"
	  + "WHERE a.alue = ? GROUP BY a.tunnus" 
          + "ORDER BY v.viimeisin_viesti DESC LIMIT ?, ?;"
        );
        
        stmt.setInt(1, alue.getTunnus());
        stmt.setInt(2, 10 * (pageNum - 1) );
        stmt.setInt(3, 10 * pageNum);
        
        ResultSet rs = stmt.executeQuery();
        
        List<Aihe> aiheet = new ArrayList<>();
        
        while (rs.next()) {
            Integer tunnus = rs.getInt("tunnus");
            String aloittaja = rs.getString("aloittaja");
            String sisalto = rs.getString("sisalto");
            String otsikko = rs.getString("otsikko");
            Timestamp luotu = rs.getTimestamp("luotu");
            Timestamp viimViesti = rs.getTimestamp("viimeisin_viesti");
            Integer viestimaara = rs.getInt("viestimaara");

            Aihe aihe = new Aihe(tunnus, aloittaja, sisalto, otsikko, 
                                 luotu, viimViesti, viestimaara);
            aiheet.add(aihe);
            
            aihe.setAlue(alue);
        }
        
        rs.close();
        stmt.close();
        connection.close();

        return aiheet;
    }
}
