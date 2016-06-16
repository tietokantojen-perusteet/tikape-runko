package tikape.runko.domain;

import java.util.Date;

public class Viesti {
    private int id;
    private Integer kayttaja;
    private Integer keskustelu;
    private String sisalto;
    private Date kellonaika;

    
    public Viesti(int id, Integer kayttaja, Integer keskustelu, String sisalto, Date kellonaika) {
        this.id = id;
        this.kayttaja = kayttaja;
        this.keskustelu = keskustelu;
        this.sisalto = sisalto;
        this.kellonaika = kellonaika;
    }
    
    public Viesti(int id, String sisalto, Date kellonaika){
        this.id = id;
        this.sisalto = sisalto;
        this.kellonaika = kellonaika;
    }
    
    public String toString() {
        return this.id + kayttaja.getTunnus() + keskustelu.getOtsikko() + sisalto + kellonaika.toString();
    }
    public int getId() {
        return id;
    }
    
    public Kayttaja getKayttaja() {
        return kayttaja;
    }
    
    public Keskustelu getKeskustelu(){
        return keskustelu;
    }
    
}
