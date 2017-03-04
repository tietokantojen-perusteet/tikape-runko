/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author aleksimu
 */
public class Ketju {
    private int ketju;
    private String aihe;
    private String otsikko;
    private String sisalto;
    private String aloitusaika;
    private String kayttajanimi;
   
    public Ketju(Integer ketju, String aihe, String otsikko, String sisalto, String aloitusaika, String kayttajanimi) {
        this.ketju = ketju;
        this.aihe = aihe;
        this.otsikko = otsikko;
        this.sisalto = sisalto;
        this.aloitusaika = aloitusaika;
        this.kayttajanimi = kayttajanimi;
    }

    public Integer getKetju() {
        return ketju;
    }

    public void setKetju(Integer id) {
        this.ketju = id;
    }

    public String getAihe() {
        return aihe;
    }

    public void setAihe(String nimi) {
        this.aihe = nimi;
    }
    
    public String getOtsikko(){
        return otsikko;
    }
    
    public void setOtsikko(String otsikko){
        this.otsikko = otsikko;
    }
    
    public String getSisalto(){
        return sisalto;
    } 
    
    public void setSisalto(String sisalto){
        this.sisalto = sisalto;
    }
    
    public String getAloitusaika(){
        return aloitusaika;
    } 
    
    public void setAloitusaika(String aloitusaika){
        this.aloitusaika = aloitusaika;
    }
    public String getKayttajanimi(){
        return this.kayttajanimi;
    } 
    
    public void setKayttajanimi(String kayttajanimi){
        this.kayttajanimi = kayttajanimi;
    }
    
}

