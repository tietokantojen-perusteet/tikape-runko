
package tikape.runko.dao;
import tikape.runko.domain.*;
import java.sql.*;
import java.util.*;
import tikape.runko.database.Database;

/* TODO: jos tulee jotain mieleen, niin kirjoita tähän
*/
public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {
    private Database database;
    private Dao<Annos, Integer> annosDao;
    private Dao<RaakaAine, Integer> raakaAineDao;
    
        public AnnosRaakaAineDao(Database database, Dao<Annos, Integer> annosDao, Dao<RaakaAine, Integer> raakaAineDao){
            this.database = database;
            this.annosDao = annosDao;
            this.raakaAineDao = raakaAineDao;
        }
        
        @Override
        public AnnosRaakaAine findOne(Integer Key) throws SQLException{
            /*  AnnosRaakaAine - olioilla ei ole toteutuksessa Primary Keytä
                Emme siis tue niiden etsintää yhdellä avaimella, sillä niillä ei ole vastaavaa avainta tieokannassa
            */
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
        public AnnosRaakaAine findOne(Integer annosID, Integer raakaAineID) throws SQLException {
            // Palauttaa vain yhden AnnosRaakaAine - olion FK:den perusteella
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annosId = ? AND raakaAineId = ?");
            stmt.setInt(1, annosID);
            stmt.setInt(2, raakaAineID);

            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }

            AnnosRaakaAine ara = new AnnosRaakaAine(annosDao.findOne(annosID), raakaAineDao.findOne(raakaAineID),
                    rs.getInt("jarjestys"), rs.getString("maara"), rs.getString("ohje"));
            stmt.close();
            rs.close();
            conn.close();

            return ara;
        }
    
        @Override
        public List<AnnosRaakaAine> findAll() throws SQLException {
            //Palauttaa taulun kaikki AnnosRaakaAine - oliot, eli kaikkien annosten kaikki raaka-aineet
            List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
            List<Annos> annokset = new ArrayList<>();
            List<RaakaAine> raakaAineet = new ArrayList<>();
            Annos annosVerrokki = null;
            RaakaAine raakaAineVerrokki = null;
            annokset = annosDao.findAll();
            raakaAineet = raakaAineDao.findAll();
            
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT * FROM AnnosRaakaAine;");
            ResultSet rs = stmt.executeQuery();
            
            // Silmukka pitää vielä debugata, tuleeko esim. tilanteita joissa annokseen tulee vääriä raaka-aineita tai toisinpäin
            while (rs.next()){
                for (Annos a : annokset){
                    if (a.getId() == rs.getInt("annosId")){
                        annosVerrokki = a;
                    }
                }
                for (RaakaAine ra : raakaAineet){
                    if (ra.getId() == rs.getInt("raakaAineId")){
                        raakaAineVerrokki = ra;
                    }
                }
                AnnosRaakaAine ara = new AnnosRaakaAine(annosVerrokki, raakaAineVerrokki,
                                     rs.getInt("jarjestys"), rs.getString("maara"), rs.getString("ohje"));
                annosRaakaAineet.add(ara);
            }

            stmt.close();
            rs.close();
            conn.close();

            return annosRaakaAineet;
        }

        @Override
        public void delete(Integer key) throws SQLException{
            // Poistaa kaikki tiettyyn annosId:seen liittyvät rivit (eli annoksen ja kaikki sen raaka-aineet)
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annosId = ?");

            stmt.setInt(1, key);
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        }
        
        @Override
        public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine ara) throws SQLException{
            if (findOne(ara.getAnnos().getId(), ara.getRaakaAine().getId()) == null){
                return save(ara);
            } else{
                return update(ara);
            }
        }
        
        public AnnosRaakaAine save(AnnosRaakaAine ara) throws SQLException{
            // tallentaa uuden raaka-aineen tietokantaan
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine"
                    + "(annosId, raakaAineId, jarjestys, maara, ohje)"
                    + " VALUES (?, ?, ?, ?, ?");
            stmt.setInt(1, ara.getAnnos().getId());
            stmt.setInt(2, ara.getRaakaAine().getId());
            stmt.setInt(3, ara.getJarjestys());
            stmt.setString(4, ara.getMaara());
            stmt.setString(5, ara.getOhje());
            
            stmt.executeUpdate();
            stmt.close();
            
            stmt = conn.prepareStatement("SELECT * From AnnosRaakaAine WHERE annosId = ? AND raakaAineId = ?");
            stmt.setInt(1, ara.getAnnos().getId());
            stmt.setInt(2, ara.getRaakaAine().getId());
            
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            AnnosRaakaAine a = new AnnosRaakaAine(annosDao.findOne(rs.getInt("annosId")), raakaAineDao.findOne(rs.getInt("raakaAineId")), rs.getInt("jarjestys"), rs.getString("maara"), rs.getString("ohje"));
            
            stmt.close();
            rs.close();
            conn.close();
            
            return a;
        }
        
        public AnnosRaakaAine update(AnnosRaakaAine ara) throws SQLException{
            //Korjaa raaka-aineen tiedot halutuiksi tietokannassa
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE AnnosRaakaAine SET "
                    + "jarjestys = ?, maara = ?, ohje = ?"
                    + "WHERE annosId = ? AND raakaAineId = ?");
            stmt.setInt(1, ara.getJarjestys());
            stmt.setString(2, ara.getMaara());
            stmt.setString(3, ara.getOhje());
            stmt.setInt(4, ara.getAnnos().getId());
            stmt.setInt(5, ara.getRaakaAine().getId());
            
            stmt.executeUpdate();
            stmt.close();
            conn.close();
                    
            return ara;
        }
        
        public void poistaRaakaAineAnnoksesta(RaakaAine raakaAine, Integer key) throws SQLException{
            //poistaa tietyn raaka-aineen annoksesta, mutta ei raaka-aine taulusta
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annosId = ? AND raakaAineId = ?");
            stmt.setInt(1, key);
            stmt.setInt(2, raakaAine.getId());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }
        
        public List<AnnosRaakaAine> annoksenRaakaAineet(Annos annos) throws SQLException{
            //Palauttaa kaikki tietyn annoksen AnnosRaakaAine - oliot
            List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT * FROM AnnosRaakaAine WHERE AnnosId = ?");
            stmt.setInt(1, annos.getId());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()){
                AnnosRaakaAine ara = new AnnosRaakaAine(annos, raakaAineDao.findOne(rs.getInt("raakaAineId")),
                        rs.getInt("jarjestys"), rs.getString("maara"), rs.getString("ohje"));
                annosRaakaAineet.add(ara);
            }
            stmt.close();
            rs.close();
            conn.close();
            
            return annosRaakaAineet;
        }
        
        public Integer raakaAineenEsiintymat(RaakaAine raakaAine) throws SQLException{
            // Kertoo, kuinka monessa annoksessa raaka-aine esiintyy, palauttaa integer - luvun.
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as lkm FROM AnnosRaakaAine WHERE RaakaAineId = ?");
            stmt.setInt(1, raakaAine.getId());
            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne){
                return 0;
            }
            int laskuri = rs.getInt("lkm");
            return laskuri;
        }
            
        

    }
