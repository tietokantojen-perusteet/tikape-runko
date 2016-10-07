/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author tamella
 */
public class Keskustelu{
    private Integer keskustelu_id;
    private String otsikko;
    private String aloittaja;
    private String aloitusviesti;
    private Alue omaalue;
    private Timestamp paivamaara;
//    private Date paivamaara;
    

    public Keskustelu(Integer id, String otsikko, String aloittaja, String aloitusviesti, Timestamp paivays) {
        this.keskustelu_id = id;
        this.otsikko = otsikko;
        this.aloittaja = aloittaja;
        this.aloitusviesti = aloitusviesti;
        this.paivamaara = paivays;
    }
    
    public Integer getId() {
        return keskustelu_id;
    }

    public void setId(Integer id) {
        this.keskustelu_id = id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String nimi) {
        this.otsikko = nimi;
    }
    public String getAloittaja() {
        return aloittaja;
    }

    public void setAloittaja(String nimi) {
        this.aloittaja = nimi;
    }
    public String getAloitusviesti() {
        return aloitusviesti;
    }

    public void setAloitusviesti(String viesti) {
        this.aloitusviesti = viesti;
    }
    public Alue getOmaalue() {
        return omaalue;
    }

    public void setOmaalue(Alue alue) {
        this.omaalue = alue;
    }
    public Timestamp getDate() {
        return paivamaara;
    }

    public void setDate(Timestamp ts) {
        this.paivamaara = ts;
    }
}
