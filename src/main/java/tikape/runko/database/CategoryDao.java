package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Category;

public class CategoryDao implements Dao<Category, Integer> {

    private final Database database;
    private SubCategoryDao subCategoryDao;

    public CategoryDao(Database db) {
        this.database = db;
        subCategoryDao = new SubCategoryDao(db);
    }

    /**
     * Hakee tietyn kategorian ID:n avulla
     * @param key
     * @return
     * @throws SQLException 
     */
    @Override
    public Category findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM categories WHERE categoryId = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("categoryId");
        String title = rs.getString("title");
        Category cat = new Category(id, title);
        //Lisätään myös alakategoriat Category-olioon.
        cat.setSubCategories(subCategoryDao.findAllByCategoryId(id));

        rs.close();
        stmt.close();
        connection.close();

        return cat;
    }

    /**
     * Hakee jokaisen kategorian sekä hakee niiden alakategoriat
     * @return
     * @throws SQLException 
     */
    @Override
    public List<Category> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM categories");
        ResultSet rs = stmt.executeQuery();
        
        List<Category> categories = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("categoryId");
            String title = rs.getString("title");
            //Lisätään myös alakategoriat Category-olioon.
            categories.add(new Category(id, title).setSubCategories(subCategoryDao.findAllByCategoryId(id)));

        }

        rs.close();
        stmt.close();
        connection.close();

        return categories;
    }

    /**
     * Poista kategorian
     * @param key
     * @throws SQLException 
     */
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Lisää kategorian
     * @param c
     * @return
     * @throws SQLException 
     */
    public boolean add(Category c) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO categories (categoryId, title) VALUES (NULL, ?)");
        stmt.setString(1, c.getName());
        return stmt.execute();
    }

}
