
package tikape.runko.domain;

public class Smoothie {
    private Integer id;
    private String name;
    private String instructions;
    
    public Smoothie(Integer id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
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