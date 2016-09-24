package tikape.runko.database;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tikape.runko.Auth;
import tikape.runko.domain.User;

public class UserDao implements Dao<User, Integer> {

    private Database database;

    public UserDao(Database database) {
        this.database = database;
    }

    @Override
    public User findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        User o = new User(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<User> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users");

        ResultSet rs = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            users.add(new User(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return users;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    public void add(String username, String password) throws SQLException {
        username = username.trim();
        //Luodaan salasanalle "suola"
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        try {
            //Yhdistetään suola ja salasanan bittijono
            byte[] passwd = password.getBytes("UTF-8");
            byte[] saltedPassword = Auth.combineTwoByteArrays(salt, passwd);
            //Käytetään SHA-256 salausta:
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = digest.digest(saltedPassword);
            //Lopuksi käytetään Base64-enkoodausta jotta salasana ja suola voidaan tallentaa tietokantaan.
            Encoder en = java.util.Base64.getEncoder();
            String saltBase64 = en.encodeToString(salt);
            String hashedPasswordBase64 = en.encodeToString(hashedPassword);

            String query = "INSERT INTO users (userId, username, password, salt, userLevel) VALUES(NULL, ?, ?, ?, 0)";
            Connection con = database.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPasswordBase64);
            stmt.setString(3, saltBase64);
            stmt.execute();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

}
