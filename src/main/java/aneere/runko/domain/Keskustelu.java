package aneere.runko.domain;

import java.util.*;

public class Keskustelu {

    private Integer keskusteluID;
    private String otsikko;
    private String aihealue;

    public Keskustelu(Integer ID, String otsikko, String aihealue) {
        this.keskusteluID = ID;
        this.otsikko = otsikko;
        this.aihealue = aihealue;
    }
    public Keskustelu(String aihealue) {
        this.aihealue = aihealue;
    }
    public Integer getID() {
        return keskusteluID;
    }

    public void setID(Integer ID) {
        this.keskusteluID = ID;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public String getAihealue() {
        return aihealue;
    }

    public void setAihealue(String aihealue) {
        this.aihealue = aihealue;
    }
    
    public String toString(){
        return this.keskusteluID + " " + this.otsikko + " " + this.aihealue;
    }

}
