
package tikape.runko;

public class RaakaAine {
    private Integer id;
    private String nimi;
    
    public RaakaAine(Integer id, String nimi) {
        this.id=id;
        this.nimi=nimi;
    }
    
    public RaakaAine(String nimi) {
        this.id=null;
        this.nimi=nimi;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id=id;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public void setNimi(String nimi) {
        this.nimi=nimi;
    }
    
    public String toString(){
        return this.nimi;
    }
}
