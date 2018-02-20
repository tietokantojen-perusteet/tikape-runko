
package tikape.runko;

public class RaakaAine {
    public Integer id;
    public String nimi;
    
    public RaakaAine(Integer id, String nimi) {
        this.id=id;
        this.nimi=nimi;
    }
    
    public RaakaAine(String nimi) {
        this.id=null;
        this.nimi=nimi;
    }
    
    public String toString(){
        return this.nimi;
    }
}
