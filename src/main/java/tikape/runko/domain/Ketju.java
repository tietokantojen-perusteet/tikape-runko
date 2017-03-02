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
    private String nimi;
    private String otsikko;
    private String sisalto;
   
    public Ketju(Integer ketju, String nimi, String otsikko, String sisalto) {
        this.ketju = ketju;
        this.nimi = nimi;
        this.otsikko = otsikko;
        this.sisalto = sisalto;
    }

    public Integer getKetju() {
        return ketju;
    }

    public void setKetju(Integer id) {
        this.ketju = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
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
    
}

