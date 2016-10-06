/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author maijhuot
 */
public class Vastaus {
    
    private Integer id;
    private Integer avaus;
    private String teksti;
    private String ajankohta;
    private String kirjoittaja;
    
    public Vastaus (Integer id, Integer avaus, String teksti, String ajankohta, String kirjoittaja) {
        this.id = id;
        this.avaus = avaus;
        this.teksti = teksti;
        this.ajankohta = ajankohta;
        this.kirjoittaja = kirjoittaja;
    }
    
    public Vastaus() {
    }
    
    public Vastaus (Integer id, Integer avaus, String teksti, String kirjoittaja) {
        this(null, avaus, teksti, null, kirjoittaja);
    }
    
    public Integer getAvaus() {
        return this.avaus;
    }
    
    public String getKirjoittaja() {
        return this.kirjoittaja;
    }
    
    public String getTeksti() {
        return this.teksti;
    }
    
    public String getAjankohta() {
        return this.ajankohta;
    }
    
    public void setAvaus(Integer avaus) {
        this.avaus = avaus;
    }
    
    public void setKirjoittaja(String kirjoittaja) {
        this.kirjoittaja = kirjoittaja;
    }
    
    public void setTeksti(String teksti) {
        this.teksti teksti;
    }
    
    public void setAjankohta(String ajankohta) {
        this.ajankohta = ajankohta;
    }
}
