
package tikape.foorumirunko.domain;

public class Alue {
    
    private int id;
    private int viestienMaara;
    private String viimeisinViesti;
    
    public Alue(int tunnus) {
        this.id = tunnus;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getViestienMaara() {
        return this.viestienMaara;
    }
    
    public String getViimeisinViesti() {
        return this.viimeisinViesti;
    }
    
}
