package tikape.runko.domain;

import java.util.Date;

public class Viesti {
    private int id;
    private Kayttaja lahettaja;
    private Keskustelu keskustelu;
    private String sisalto;
    private Date kellonaika;
    
    public Viesti(int id, Kayttaja lahettaja, Keskustelu keskustelu, String sisalto, Date kellonaika) {
        this.id = id;
        this.lahettaja = lahettaja;
        this.keskustelu = keskustelu;
        this.sisalto = sisalto;
        this.kellonaika = kellonaika;
    }
    public String toString() {
        return this.id + lahettaja.getTunnus() + keskustelu.getOtsikko() + sisalto + kellonaika.toString();
    }
    public int getId() {
        return id;
    }
    
    public Kayttaja getLahettaja() {
        return lahettaja;
    }
}
