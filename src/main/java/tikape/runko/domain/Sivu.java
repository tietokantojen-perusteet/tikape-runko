
package tikape.runko.domain;

import java.util.ArrayList;


public class Sivu {
    private int ekaRivi;
    private int vikaRivi;
    private String edellinenSivu;
    private String seuraavaSivu;

    public Sivu(int ekaRivi, int vikaRivi, String edellinenSivu, String seuraavaSivu) {
        this.ekaRivi = ekaRivi;
        this.vikaRivi = vikaRivi;
        this.edellinenSivu = edellinenSivu;
        this.seuraavaSivu = seuraavaSivu;
    }

    public Sivu() {
    }

    
    
    public void laskeSivu(int rivit, int haluttuSivu) {
        int sivuja = (rivit-1)/10+1;
        if (haluttuSivu>sivuja) {
            haluttuSivu = sivuja;
        }
        this.ekaRivi = (haluttuSivu-1)*10;
        this.vikaRivi = haluttuSivu*10-1;
        if(this.vikaRivi>=rivit) {
            this.vikaRivi = rivit-1;
        }        
        this.edellinenSivu = null;
        if(haluttuSivu>1) {
            this.edellinenSivu = "" + (haluttuSivu-1);
        }
        this.seuraavaSivu = null;
        if(haluttuSivu<sivuja) {
            this.seuraavaSivu = "" + (haluttuSivu+1);
        }
    }

    public String getEdellinenSivu() {
        return edellinenSivu;
    }

    public int getEkaRivi() {
        return ekaRivi;
    }

    public String getSeuraavaSivu() {
        return seuraavaSivu;
    }

    public int getVikaRivi() {
        return vikaRivi;
    }

    public void setEdellinenSivu(String edellinenSivu) {
        this.edellinenSivu = edellinenSivu;
    }

    public void setSeuraavaSivu(String seuraavaSivu) {
        this.seuraavaSivu = seuraavaSivu;
    }
   
    
    
}
