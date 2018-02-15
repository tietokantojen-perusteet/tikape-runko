package tikape.runko;

public class Annos {
    String nimi;
    public Integer id;
    
    public Annos(String nimi){
        this.nimi = nimi;
    }
    public Annos(int id, String nimi){
        this.id = id;
        this.nimi = nimi;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNimi(String nimi){
        this.nimi = nimi;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getNimi(){
        return this.nimi;
    }
}
