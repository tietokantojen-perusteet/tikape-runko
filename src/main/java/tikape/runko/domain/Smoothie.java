
package tikape.runko.domain;

import java.util.ArrayList;
import java.util.List;

public class Smoothie {
    private Integer id;
    private String name;
    private String instructions;
    private List<Ingredient> ingredients;
    
    public Smoothie(Integer id, String name, String instructions, ArrayList ingredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }
}