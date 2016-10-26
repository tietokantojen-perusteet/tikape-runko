package tikape.runko.domain;

import java.sql.*;

public class Viesti {
    private Integer tunnus;
    private Aihe aihe;
    private String teksti;
    private String lahettaja;
    private Timestamp lahetetty;
    
//    public Viesti(String teksti, String lahettaja, Timestamp lahetetty) {
//        this(null, teksti, lahettaja, lahetetty);
//    }
    
    public Viesti(Integer tunnus, String teksti, String lahettaja, Timestamp lahetetty) {
        
        this.tunnus = tunnus;
        this.aihe = null;
        this.teksti = teksti;
        this.lahetetty = lahetetty;
        this.lahettaja = lahettaja;
    }
    
    public Viesti(Aihe aihe, String teksti, String lahettaja, Timestamp lahetetty) {
        this.aihe = aihe;
        this.teksti = teksti;
        this.lahettaja = lahettaja;
        this.lahetetty = lahetetty;
    }
    
    public Viesti(Aihe aihe, String teksti, String lahettaja) {
        this.aihe = aihe;
        this.teksti = teksti;
        this.lahettaja = lahettaja;
    }
    
    public Integer getTunnus() {
        return tunnus;
    }
    
    public void setTunnus(Integer tunnus) {
        this.tunnus = tunnus;
    }
    
    public Aihe getAihe() {
        return aihe;
    }
    
    public void setAihe(Aihe aihe) {
        this.aihe = aihe;
    }
    
    public String getTeksti() {
        return teksti;
    }
    
    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }
    
    public String getLahettaja() {
        return lahettaja;
    }
    
    public void setLahettaja(String lahettaja) {
        this.lahettaja = lahettaja;
    }
    
    public Timestamp getLahetetty() {
        return lahetetty;
    }
    
    public void setLahetetty(Timestamp lahetetty) {
        this.lahetetty = lahetetty;
    }
}
