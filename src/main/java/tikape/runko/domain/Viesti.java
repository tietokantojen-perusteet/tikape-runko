package tikape.runko.domain;

public class Viesti {
    private Integer id;
    private String lahettaja;
    
    public Viesti(Integer id, String lahettaja) {
        this.id = id;
        this.lahettaja = lahettaja;
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getLahettaja() {
        return lahettaja;
    }
}
