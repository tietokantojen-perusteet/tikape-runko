/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;
import java.util.Date;
/**
 *
 * @author tamella
 */
public class Viesti {
    private Integer viesti_id;
    private Keskustelu omakeskustelu;
    private Date julkaisuaika;
    private String kirjoittaja;
    private String teksti;

    public Viesti(Integer id, Date aika, String kirjo, String tekstii) {
        this.viesti_id = id;
        this.julkaisuaika = aika;
        this.kirjoittaja = kirjo;
        this.teksti = tekstii;
    }

    public Integer getId() {
        return viesti_id;
    }

    public void setId(Integer id) {
        this.viesti_id = id;
    }

    public Keskustelu getOmakeskustelu() {
        return omakeskustelu;
    }

    public void setOmakeskustelu(Keskustelu mess) {
        this.omakeskustelu = mess;
    }
    public Date getViestiDate() {
        return julkaisuaika;
    }

    public void setViestiDate(Date date) {
        this.julkaisuaika = date;
    }
    public String getKirjoittaja() {
        return kirjoittaja;
    }

    public void setKirjoittaja(String kirjo) {
        this.kirjoittaja= kirjo;
    }
    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String tekstii) {
        this.teksti = tekstii;
    }
}
