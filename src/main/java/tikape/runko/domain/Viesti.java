package tikape.runko.domain;

import java.sql.Date;
import java.sql.Timestamp;


public class Viesti {
    private int id;
    private Integer kayttaja;
    private Integer keskustelu;
    private String sisalto;
    private Timestamp kellonaika;

    
    public Viesti(int id, Integer kayttaja, Integer keskustelu, String sisalto, Timestamp kellonaika) {
        this.id = id;
        this.kayttaja = kayttaja;
        this.keskustelu = keskustelu;
        this.sisalto = sisalto;
        this.kellonaika = kellonaika;
    }
    public String toString() {
        return this.id + this.kayttaja + this.keskustelu + this.sisalto + this.kellonaika;
    }
//    public Viesti(int id, String sisalto, Date kellonaika){
//        this.id = id;
//        this.sisalto = sisalto;
//        this.kellonaika = kellonaika;
//    }
    
//    public String toString() {
//        return this.id + kayttaja.getTunnus() + keskustelu.getOtsikko() + sisalto + kellonaika.toString();
//    }
    public int getID() {
        return id;
    }
    
    public Integer getKayttajaID() {
        return kayttaja;
    }
//    
    public Integer getKeskusteluID(){
        return keskustelu;
    }
    
    public String getSisalto(){
        return this.sisalto;
    }
    
    public Timestamp getKellonaika(){
        return this.kellonaika;
    }
    
}
