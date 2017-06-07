/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.sql.Date;

/**
 *
 * @author Kim
 */
public class Viesti {
    
    private Integer id;
    private String kayttaja;
    private String teksti;
    private Date paivamaara;
    private Integer ketjunid;
    
    public Viesti(Integer id, String kayttaja, String teksti, Date paivamaara, Integer ketjuid) {
        this.id = id;
        this.kayttaja = kayttaja;
        this.teksti = teksti;
        this.paivamaara = paivamaara;
        this.ketjunid = ketjuid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setKayttaja(String kayttaja) {
        this.kayttaja = kayttaja;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }

    public void setPaivamaara(Date paivamaara) {
        this.paivamaara = paivamaara;
    }

    public void setKetjunid(Integer ketjunid) {
        this.ketjunid = ketjunid;
    }

    public Integer getId() {
        return id;
    }

    public String getKayttaja() {
        return kayttaja;
    }

    public String getTeksti() {
        return teksti;
    }

    public Date getPaivamaara() {
        return paivamaara;
    }

    public Integer getKetjunid() {
        return ketjunid;
    }
    
}
