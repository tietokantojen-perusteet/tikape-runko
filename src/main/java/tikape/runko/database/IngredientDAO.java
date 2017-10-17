
package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Ingredient;

public class IngredientDAO implements DAO<Ingredient, Integer> {
    private final Database database;
    
    public IngredientDAO(Database database) {
        this.database = database;
    }

    @Override
    public void create(Ingredient object) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Ingredient (name) VALUES (?);");
        statement.setString(1, object.getName());
        statement.executeUpdate();
        
        statement.close();
        connection.close();
    }

    @Override
    public Ingredient read(Integer key) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ingredient where id = ?;");
        statement.setInt(1, key);
        
        ResultSet resultSet = statement.executeQuery();
        
        Ingredient ingredient = null;
        
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            ingredient = new Ingredient(id, name);
        }
        
        resultSet.close();
        statement.close();
        connection.close();
        
        return ingredient;
    }

    @Override
    public List<Ingredient> readAll() throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ingredient;");
        
        ResultSet resultSet = statement.executeQuery();
        
        List<Ingredient> ingredients = new ArrayList<>();
        
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            ingredients.add(new Ingredient(id, name));
        }
        
        resultSet.close();
        statement.close();
        connection.close();
        
        return ingredients;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
