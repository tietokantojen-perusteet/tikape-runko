package aneere.runko.domain;

import java.sql.Timestamp;
import java.util.Calendar;

public class Viesti {
    private int ID;
    private Integer kayttaja;
    private Integer keskustelu;
    private String sisalto;
    private Timestamp kellonaika;
    private String tunnus;

    
    public Viesti(int ID, Integer kayttaja, Integer keskustelu, String sisalto) {
        this.ID = ID;
        this.kayttaja = kayttaja;
        this.keskustelu = keskustelu;
        this.sisalto = sisalto;
        Calendar calendar = Calendar.getInstance();
        this.kellonaika = new java.sql.Timestamp(calendar.getTime().getTime());
    }
    public Viesti(int ID, Integer kayttaja, Integer keskustelu, Timestamp kellonaika, String sisalto) {
        this.ID = ID;
        this.kayttaja = kayttaja;
        this.keskustelu = keskustelu;
        this.sisalto = sisalto;
        this.kellonaika = kellonaika;
    }
    
    public Viesti(int ID, Integer kayttaja, Integer keskustelu, Timestamp kellonaika, String sisalto, String tunnus) {
        this.ID = ID;
        this.kayttaja = kayttaja;
        this.keskustelu = keskustelu;
        this.sisalto = sisalto;
        this.kellonaika = kellonaika;
        this.tunnus = tunnus;
    }
    public String toString() {
        return "ID:" + this.ID + " " + this.sisalto + "   " + this.kellonaika + " " +  this.tunnus;
    }
    public int getID() {
        return ID;
    }
    
    public Integer getKayttajaID() {
        return kayttaja;
    }
//    
    public Integer getKeskusteluID(){
        return keskustelu;
    }
    
    public String getSisalto(){
        return this.sisalto;
    }
    
    public Timestamp getKellonaika(){
        return this.kellonaika;
    }
    
}
