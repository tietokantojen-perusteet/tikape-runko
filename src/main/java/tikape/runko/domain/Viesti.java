/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;
import java.sql.Time;
import java.sql.Timestamp;
// *
 //* @author tamella
 //*/
public class Viesti implements Comparable<Viesti>{
    private Integer viesti_id;
    private Keskustelu omakeskustelu;
    private Timestamp julkaisuaika;
    private String kirjoittaja;
    private String teksti;

    public Viesti(Integer id, Timestamp aika, String kirjo, String tekstii) {
        this.viesti_id = id;
        this.julkaisuaika = aika;
        this.kirjoittaja = kirjo;
        this.teksti = tekstii;
    }
    @Override
    public int compareTo(Viesti v) {
    return getViestiTime().compareTo(v.getViestiTime());
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
    public Timestamp getViestiTime() {
        return julkaisuaika;
    }

    public void setViestiTime(Timestamp aika) {
        this.julkaisuaika = aika;
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
