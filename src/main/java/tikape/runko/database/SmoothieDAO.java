
package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Ingredient;
import tikape.runko.domain.Smoothie;

public class SmoothieDAO implements DAO<Smoothie, Integer> {
    private final Database database;
    
    public SmoothieDAO(Database database) {
        this.database = database;
    }

    @Override
    public void create(Smoothie object) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Smoothie (nimi, ohje) VALUES (?, ?);");
        statement.setString(1, object.getName());
        statement.setString(2, object.getInstructions());
        statement.executeUpdate();
        
        statement.close();
        connection.close();
        
    }

    @Override
    public Smoothie read(Integer key) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Smoothie where id = ?;");
        statement.setInt(1, key);
        
        ResultSet resultSet = statement.executeQuery();
        
        Smoothie smoothie = null;
        
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("nimi");
            String instructions = resultSet.getString("ohje");
            ArrayList<Ingredient> ingredients = new ArrayList<>(); //tässä lisätään vain tyhjä lista, koska en osannut tehdä toiminnallisuutta, ohjelma olisi herjannut, jos en olisi tehnyt näin.
            smoothie = new Smoothie (id, name, instructions, ingredients);
        }
        
        resultSet.close();
        statement.close();
        connection.close();
        
        return smoothie;
    }

    @Override
    public List<Smoothie> readAll() throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Smoothie;");
        
        ResultSet resultSet = statement.executeQuery();
        
        List<Smoothie> smoothies = new ArrayList<>();
        
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("nimi");
            String instructions = resultSet.getString("ohje");
            ArrayList<Ingredient> ingredients = new ArrayList<>(); //tässä lisätään vain tyhjä lista, koska en osannut tehdä toiminnallisuutta, ohjelma olisi herjannut, jos en olisi tehnyt näin.
            smoothies.add(new Smoothie(id, name, instructions, ingredients));
        }
        
        resultSet.close();
        statement.close();
        connection.close();
        
        return smoothies;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Smoothie WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    
}
