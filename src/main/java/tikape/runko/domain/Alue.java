/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author tamella
 */
public class Alue {
    private Integer alue_id;
    private String nimi;

    public Alue(Integer id, String nimi) {
        this.alue_id = id;
        this.nimi = nimi;
    }

    public Integer getId() {
        return alue_id;
    }

    public void setId(Integer id) {
        this.alue_id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
}
