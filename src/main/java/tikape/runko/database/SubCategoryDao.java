package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.SubCategory;

public class SubCategoryDao implements Dao<SubCategory, Integer> {

    private final Database database;

    public SubCategoryDao(Database db) {
        this.database = db;
    }

    /**
     * Hakee alakategorian ID:n avulla
     * @param key
     * @return
     * @throws SQLException 
     */
    @Override
    public SubCategory findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM subCategories WHERE subCatId = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer subCatId = rs.getInt("categoryId");
        Integer mainCatId = rs.getInt("catId");
        String title = rs.getString("title");
        String description = rs.getString("description");
        SubCategory cat = new SubCategory(mainCatId, subCatId, title).setDescription(description);

        rs.close();
        stmt.close();
        connection.close();

        return cat;
    }

    /**
     * Hakee kaikki alakategoriat
     * @return
     * @throws SQLException 
     */
    @Override
    public List<SubCategory> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM subCategories");
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();

        List<SubCategory> categories = new ArrayList<>();
        while (rs.next()) {
            Integer mainCatId = rs.getInt("catId");
            Integer subCatId = rs.getInt("categoryId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            categories.add(new SubCategory(mainCatId, subCatId, title).setDescription(description));
        }

        rs.close();
        stmt.close();
        connection.close();

        return categories;
    }

    /**
     * Listaa kaikki alakategoriat halutulle pääkategorian ID:lle
     *
     * @param categoryId
     * @return
     * @throws SQLException
     */
    public List<SubCategory> findAllByCategoryId(int categoryId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM subCategories WHERE catId = ?");
        stmt.setInt(1, categoryId);
        ResultSet rs = stmt.executeQuery();

        List<SubCategory> categories = new ArrayList<>();
        while (rs.next()) {
            Integer mainCatId = rs.getInt("catId");
            Integer subCatId = rs.getInt("subCatId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            categories.add(new SubCategory(mainCatId, subCatId, title).setDescription(description));
        }

        rs.close();
        stmt.close();
        connection.close();

        return categories;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Lisää alakategorian
     * @param c
     * @return
     * @throws SQLException 
     */
    public boolean add(SubCategory c) throws SQLException {
        Connection connection = database.getConnection();
        int mainCatId = c.getCategoryId();
        String title = c.getName();
        String desc = c.getDescription();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO subCategories (subCatId, catId, title , description) VALUES (NULL, ?, ?, ?)");
        stmt.setInt(1, mainCatId);
        stmt.setString(2, title);
        stmt.setString(3, desc);
        return stmt.execute();
    }

}
