package tikape.runko.domain;

public class Viesti {
    private int id;
    private int lahettaja;
    
    public Viesti(int id, int lahettaja) {
        this.id = id;
        this.lahettaja = lahettaja;
    }
    
    public int getId() {
        return id;
    }
    
    public int getLahettaja() {
        return lahettaja;
    }
}
