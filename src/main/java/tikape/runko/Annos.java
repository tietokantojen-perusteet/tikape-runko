package tikape.runko;

public class Annos {
    String ohje;
    String nimi;
    public Integer id;
    
    public Annos(int id, String nimi, String ohje){
        this.id = id;
        this.nimi = nimi;
        this.ohje = ohje;
    }
	
	public Annos(String nimi, String ohje) {
		this.id = null;
		this.nimi = nimi;
		this.ohje = ohje;
	}
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNimi(String nimi){
        this.nimi = nimi;
    }
    
    public Integer getId(){
        return this.id;
    }
    
    public String getNimi(){
        return this.nimi;
    }
    
    public void setOhje(String ohje) {
        this.ohje = ohje;
    }

    public String getOhje() {
        return this.ohje;
    }
    
    public String toString(){
        return this.nimi;
    }
}
