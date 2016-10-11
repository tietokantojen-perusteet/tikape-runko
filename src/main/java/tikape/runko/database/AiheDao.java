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
        String sql = "INSERT INTO Aihe (alue, aloittaja, sisalto, otsikko, luotu) " +
                     "VALUES (?, ?, ?, ?, ?);";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe WHERE tunnus = ?");
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

        Aihe aihe = new Aihe(key, aloittaja, sisalto, otsikko, luotu);
        aihe.setAlue(alueDao.findOne(alueTunnus));

        rs.close();
        stmt.close();
        connection.close();

        return aihe;
    }

    @Override
    public List<Aihe> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe;");
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

            Aihe aihe = new Aihe(tunnus, aloittaja, sisalto, otsikko, luotu);
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

        // Luodaan IN-kyselyä varten paikat, joihin arvot asetetaan --
        // toistaiseksi IN-parametrille ei voi antaa suoraan kokoelmaa
        StringBuilder arvot = new StringBuilder("?");
        for (int i = 1; i < keys.size(); i++) {
            arvot.append(", ?");
        }

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe WHERE tunnus IN (" + arvot + ")");
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

            Aihe aihe = new Aihe(tunnus, aloittaja, sisalto, otsikko, luotu);
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
    
}
