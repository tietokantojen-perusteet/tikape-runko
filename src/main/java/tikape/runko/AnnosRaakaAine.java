/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko;

public class AnnosRaakaAine {
    private Annos annos;
    private RaakaAine raakaAine;
    public AnnosRaakaAine (Annos an, RaakaAine ra) {
        this.annos = an;
        this.raakaAine = ra;
    }
    public Annos getAnnos() {
        return this.annos;
    }
    public RaakaAine getRaakaine() {
        return this.raakaAine;
    }
}
