/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;
import java.util.ArrayList;
import java.util.List;

public class Juoma {
    private int id;
    private List<RaakaAine> lista;
    private String nimi;
    private String valmistusOhje;
    public Juoma(int id, String nimi, String valmistusOhje) {
        this.id = id;
        this.nimi = nimi;
        this.lista = new ArrayList<>();
        this.valmistusOhje = valmistusOhje;
    }
    public int getId() {
        return this.id;
    }
    public String getNimi() {
        return this.nimi;
    }
    public List getRaakaAineLista() {
        return this.lista;
    }
    public String getValmistusOhje() {
        return this.valmistusOhje;
    }
}