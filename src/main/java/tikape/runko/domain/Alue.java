package tikape.runko.domain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Alue {
    private Integer tunnus;
    private String nimi;
    private String kuvaus;
    private List<Aihe> aiheet;
    private Timestamp viimeisin_viesti;
    private Integer viestimaara;
    
    public Alue(Integer tunnus, String nimi, String kuvaus) {
        this(tunnus, nimi, kuvaus, null, null);
    }
    
    public Alue(Integer tunnus, String nimi, String kuvaus, Timestamp viimeisin_viesti,
                Integer viestimaara) {
        this.tunnus = tunnus;
        this.nimi = nimi;
        this.kuvaus = kuvaus;
        this.aiheet = new ArrayList<>();
        this.viimeisin_viesti = viimeisin_viesti;
        this.viestimaara = viestimaara;
    }
    
    public Integer getTunnus() {
        return tunnus;
    }
    
    public void setTunnus(Integer tunnus) {
        this.tunnus = tunnus;
    }
    
    public String getNimi() {
        return nimi;
    }
    
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public String getKuvaus() {
        return kuvaus;
    }
    
    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }
    
    public List<Aihe> getAiheet() {
        return aiheet;
    }
    
    public void setAiheet(List<Aihe> aiheet) {
        this.aiheet = aiheet;
    }
    
    public Integer getViestimaara() {
        return viestimaara;
    }
    
    public void setViestimaara(Integer viestimaara) {
        this.viestimaara = viestimaara;
    }
    
    public Timestamp getViimeisinViesti() {
        return viimeisin_viesti;
    }
    
    public void setViimeisinViesti(Timestamp viimeisin_viesti) {
        this.viimeisin_viesti = viimeisin_viesti;
    }
}
