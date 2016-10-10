
package tikape.runko.domain;

import java.sql.Timestamp;

public class Viesti {
    private Integer id;
    private Integer keskustelu_id;
    private Timestamp aika;
    private String kayttaja;
    private String sisalto;

    public Viesti(Integer id, Integer keskustelu_id, Timestamp aika, String kayttaja, String sisalto) {
        this.id = id;
        this.keskustelu_id = keskustelu_id;
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

    public Integer getKeskustelu_id() {
        return keskustelu_id;
    }

    public void setKeskustelu_id(Integer keskustelu_id) {
        this.keskustelu_id = keskustelu_id;
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
