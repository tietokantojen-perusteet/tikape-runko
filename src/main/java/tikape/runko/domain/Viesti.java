package tikape.runko.domain;


import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author onniaarn
 */
public class Viesti {
    private Integer ketju_id;
    private String kommentti;
    private String kayttajanimi;
    private Timestamp aika;
    
    public Viesti(Integer id, String kom, String kayttaja, Timestamp aika) {
        this.ketju_id = id;
        this.kommentti = kom;
        this.kayttajanimi = kayttaja;
        this.aika = aika;
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

    public Timestamp getAika() {
        return aika;
    }

    public void setAika(Timestamp aika) {
        this.aika = aika;
    }
    
    
}
