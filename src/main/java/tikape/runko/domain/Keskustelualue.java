/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author llmlks
 */
public class Keskustelualue {
    
    private Integer id;
    private String nimi;
    
    public Keskustelualue(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNimi() {
        return nimi;
    }

    public void setNimi(Integer nimi) {
        this.nimi = nimi;
    }
    
    
}
