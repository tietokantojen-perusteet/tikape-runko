package tikape.runko.domain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Alue {
    private Integer tunnus;
    private String nimi;
    private String kuvaus;
    private List<Aihe> aiheet;
    
    public Alue(Integer tunnus, String nimi, String kuvaus) {
        this.tunnus = tunnus;
        this.nimi = nimi;
        this.kuvaus = kuvaus;
        this.aiheet = new ArrayList<>();
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
}
