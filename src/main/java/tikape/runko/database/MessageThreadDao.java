package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Message;
import tikape.runko.domain.MessageThread;

public class MessageThreadDao implements Dao<MessageThread, Integer> {

    private final Database database;

    public MessageThreadDao(Database database) {
        this.database = database;
    }

    /**
     * Hakee tietyn viestiketjun ID:n avulla
     *
     * @param key
     * @return
     * @throws SQLException
     */
    @Override
    public MessageThread findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM threads WHERE threadId = ?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        Integer subCatId = rs.getInt("subCategoryId");
        Integer userId = rs.getInt("userId");
        String title = rs.getString("title");
        String creationDate = rs.getString("creationDate");
        String description = rs.getString("description");
        MessageThread msgThread = new MessageThread(subCatId, userId, title, creationDate);
        rs.close();
        stmt.close();

        stmt = connection.prepareStatement("SELECT * FROM posts WHERE threadId = ? ORDER BY postId ASC");
        stmt.setInt(0, key);
        rs = stmt.executeQuery();

        while (rs.next()) {
            Integer postId = rs.getInt("subCategoryId");
            userId = rs.getInt("userId");
            String body = rs.getString("body");
            String timeStamp = rs.getString("timestamp");
            msgThread.addMessage(new Message(postId, userId, body, timeStamp));
        }

        connection.close();

        return msgThread;
    }

    /**
     * Hakee kaikki viestiketjut
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<MessageThread> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Hakee kaikki viestiketjut tietystä alakategoriasta
     *
     * @param subCategoryId
     * @return
     * @throws SQLException
     */
    public List<MessageThread> findAllFromSubCategory(int subCategoryId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM threads WHERE subCategoryId = ?");
        stmt.setInt(1, subCategoryId);
        ResultSet rs = stmt.executeQuery();

        List<MessageThread> msgThreads = new ArrayList<>();
        while (rs.next()) {
            Integer subCatId = rs.getInt("subCategoryId");
            Integer userId = rs.getInt("userId");
            String title = rs.getString("title");
            String creationDate = rs.getString("creationDate");
            msgThreads.add(new MessageThread(subCategoryId, userId, title, creationDate));
        }

        rs.close();
        stmt.close();
        connection.close();

        return msgThreads;
    }

    /**
     * Poistaa viestiketjun
     *
     * @param key
     * @throws SQLException
     */
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Uuden viestiketjun luonti. Samalla lisätään viestiketjuun aloituspostaus.
     *
     * @param msgThread
     * @param subCategoryId
     * @param title
     * @param desc
     * @throws java.sql.SQLException
     */
    public void add(MessageThread msgThread) throws SQLException {
        Connection connection = database.getConnection();
        //Lisätään uusi viestiketju
        String query = "INSERT INTO threads (subCategoryId, userId, title, creationDate) VALUES (?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, msgThread.getSubCategoryId());
        stmt.setInt(2, msgThread.getUserId());
        stmt.setString(3, msgThread.getTitle());
        stmt.setString(4, msgThread.getTimeStamp());
        System.out.println(stmt.toString());
        //Poimitaan uuden viestiketjun ID kun kysely on suoritettu
        int affectedRows = stmt.executeUpdate();
        try (ResultSet insertId = stmt.getGeneratedKeys()) {
            if (insertId.next()) {
                //Tämän voisi korvata MessageDao:lla?
                query = "INSERT INTO posts (threadId, userId, timestamp, body) VALUES (?,?,?,?)";
                PreparedStatement stmt2 = connection.prepareStatement(query);
                stmt2.setInt(1, insertId.getInt(1));
                stmt2.setInt(2, msgThread.getUserId());
                //Oletetaan että MessageThread -olioon on lisätty yksi viesti
                stmt2.setString(3, msgThread.getMessages().get(0).getTimeStamp());
                stmt2.setString(4, msgThread.getMessages().get(0).getBody());
                //Suorita kysely
                stmt2.execute();
            }

        }

    }

}
