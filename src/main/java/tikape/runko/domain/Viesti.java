
package tikape.runko.domain;

import java.sql.Timestamp;

public class Viesti {
    private Integer id;
    private Keskustelu keskustelu;
    private Timestamp aika;
    private String kayttaja;
    private String sisalto;

    public Viesti(Integer id, Timestamp aika, String kayttaja, String sisalto) {
        this.id = id;
        this.aika = aika;
        this.kayttaja = kayttaja;
        this.sisalto = sisalto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Keskustelu getKeskustelu() {
        return keskustelu;
    }

    public void setKeskustelu(Keskustelu keskustelu) {
        this.keskustelu = keskustelu;
    }

    public Timestamp getAika() {
        return aika;
    }

    public void setAika(Timestamp aika) {
        this.aika = aika;
    }

    public String getKayttaja() {
        return kayttaja;
    }

    public void setKayttaja(String kayttaja) {
        this.kayttaja = kayttaja;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }
    
        
    
}
