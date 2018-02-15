package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.AnnosRaakaAine;

public class AnnosRaakaAineDao{
    private Database database;
    
    public AnnosRaakaAineDao(Database database){
        this.database = database;
    }
    
    public AnnosRaakaAine findOne(int annos_id, int raaka_aine_id) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annosId = ? AND raakaAineId = ?");
        stmt.setInt(1, annos_id);
        stmt.setInt(2, raaka_aine_id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        AnnosRaakaAine a = new AnnosRaakaAine(rs.getInt("annos_id"), rs.getInt("raaka_aine_id"), rs.getInt("jarjestys"), rs.getDouble("maara"), rs.getString("ohje"));
        
        stmt.close();
        rs.close();

        conn.close();

        return a;
    }
    
    public List<AnnosRaakaAine> findAll() throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine");
        
        List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            AnnosRaakaAine a = new AnnosRaakaAine(rs.getInt("annos_id"), rs.getInt("raaka_aine_id"), rs.getInt("jarjestys"), rs.getDouble("maara"), rs.getString("ohje"));
            annosRaakaAineet.add(a);
        }
        rs.close();
        stmt.close();
        conn.close();
        
        return annosRaakaAineet;
    }
    
    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine object) throws SQLException{
        if (object.annosId == null && object.raakaAineId == null) {
            return save(object);
        } else {
            // muulloin päivitetään asiakas
            return update(object);
        }
    }
    
    public void delete(int annos_id, int raaka_aine_id) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Annos WHERE annos_id = ? AND raaka_aine_id = ?");

        stmt.setInt(1, annos_id);
        stmt.setInt(2, raaka_aine_id);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    
    private AnnosRaakaAine save(AnnosRaakaAine annosRaakaAine) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos"
                + " (annos_id, raaka_aine_id, jarjestys, maara, ohje)"
                + " VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, annosRaakaAine.getAnnosId());
        stmt.setInt(2, annosRaakaAine.getRaakaAineId());
        stmt.setInt(3, annosRaakaAine.getJarjestys());
        stmt.setDouble(4, annosRaakaAine.getMaara());
        stmt.setString(5, annosRaakaAine.getOhje());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine"
                + " WHERE annos_id = ? AND raaka_aine_id = ? AND jarjestys = ? AND maara = ? AND ohje = ?");
        stmt.setInt(1, annosRaakaAine.getAnnosId());
        stmt.setInt(2, annosRaakaAine.getRaakaAineId());
        stmt.setInt(3, annosRaakaAine.getJarjestys());
        stmt.setDouble(4, annosRaakaAine.getMaara());
        stmt.setString(5, annosRaakaAine.getOhje());

        ResultSet rs = stmt.executeQuery();
        rs.next(); // vain 1 tulos

        AnnosRaakaAine a = new AnnosRaakaAine(rs.getInt("annos_id"), rs.getInt("raaka_aine_id"), rs.getInt("jarjestys"), rs.getDouble("maara"), rs.getString("ohje"));

        stmt.close();
        rs.close();
        conn.close();

        return a;
    }
    
    private AnnosRaakaAine update(AnnosRaakaAine annosRaakaAine) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Annos SET"
                + "jarjestys = ?, maara = ?, ohje = ? WHERE annos_id = ? AND raaka_aine_id = ?");
        stmt.setInt(1, annosRaakaAine.getJarjestys());      
        stmt.setDouble(2, annosRaakaAine.getMaara());
        stmt.setString(3, annosRaakaAine.getOhje());
        stmt.setInt(4, annosRaakaAine.getAnnosId());
        stmt.setInt(5, annosRaakaAine.getRaakaAineId());
        
        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return annosRaakaAine;
    }
}
