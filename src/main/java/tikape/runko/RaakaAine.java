/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko;

public class RaakaAine {
    private int id;
    private String nimi;
    public RaakaAine(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    public int getId() {
        return this.id;
    }
    public String getNimi() {
        return this.nimi;
    }
}