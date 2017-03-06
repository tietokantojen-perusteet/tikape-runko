package tikape.runko.domain;


/**
 *
 * @author onniaarn
 */
public class Viesti {
    private Integer ketju_id;
    private String kommentti;
    private String kayttajanimi;
    private String aika;
    
    public Viesti(Integer id, String kom, String kayttaja, String aika) {
        this.ketju_id = id;
        this.kommentti = kom;
        this.kayttajanimi = kayttaja;
//        this.aika = aika;
    };
    
    public Viesti(Integer id, String kom, String kayttaja) {
        this.ketju_id = id;
        this.kommentti = kom;
        this.kayttajanimi = kayttaja;
    };

    public Integer getKetju_id() {
        return ketju_id;
    }

    public void setKetju_id(Integer ketju_id) {
        this.ketju_id = ketju_id;
    }

    public String getKommentti() {
        return kommentti;
    }

    public void setKommentti(String kommentti) {
        this.kommentti = kommentti;
    }

    public String getKayttajanimi() {
        return kayttajanimi;
    }

    public void setKayttajanimi(String kayttajanimi) {
        this.kayttajanimi = kayttajanimi;
    }

//    public String getAika() {
//        return aika;
//    }
//
//    public void setAika(String aika) {
//        this.aika = aika;
//    }
    
    
}
