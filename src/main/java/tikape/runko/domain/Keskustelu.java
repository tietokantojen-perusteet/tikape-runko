/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author Reetta
 */
public class Keskustelu {

    private Integer id;
    private String otsikko;
    private String aihealue;

    public Keskustelu(Integer id, String otsikko, String aihealue) {
        this.id = id;
        this.otsikko = otsikko;
        this.aihealue = aihealue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return this.id + " " + this.otsikko + " " + this.aihealue;
    }

}
