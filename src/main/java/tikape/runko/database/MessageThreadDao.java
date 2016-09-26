package tikape.runko.database;

import java.sql.SQLException;
import java.util.List;
import tikape.runko.domain.Thread;

public class MessageThreadDao implements Dao<Thread, Integer> {

    private final Database db;

    public MessageThreadDao(Database db) {
        this.db = db;
    }

    @Override
    public Thread findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Thread> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Uuden viestiketjun luonti. Samalla lisätään viestiketjuun aloituspostaus.
     * @param subCategoryId
     * @param title
     * @param desc 
     */
    public void add(int subCategoryId, String title, String desc) {
        
    }

}
