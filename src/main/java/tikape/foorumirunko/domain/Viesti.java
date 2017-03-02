
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
    
}
