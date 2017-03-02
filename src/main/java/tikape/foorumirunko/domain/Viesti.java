
package tikape.foorumirunko.domain;

public class Viesti {
    
    private Kayttaja kayttaja;
    private String otsikko;
    private String sisalto;
    private String aika;
    private int alueenId;
    
    public Viesti(Kayttaja kirjoittaja) {
        this.kayttaja = kirjoittaja;
    }
    
    public Kayttaja getKayttaja() {
        return this.kayttaja;
    }
    
    public String getOtsikko() {
        return this.otsikko;
    }
    
    public String getSisalto() {
        return this.sisalto;
    }
    
    public String getAika() {
        return this.aika;
    }
    
    public int getAlueenId() {
        return this.alueenId;
    }
    
}
