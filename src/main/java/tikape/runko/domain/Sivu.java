
package tikape.runko.domain;

//apuLuokka rivien jakamiseen usealle sivulle sekä sivuilla navigointiin
public class Sivu {
    private int ekaRivi;
    private int vikaRivi;
    private String edellinenSivu;
    private String seuraavaSivu;

    // lasketaan sivulle tulevat rivit ja seuraava ja edellinen sivu, kun tiedetään rivien määrä ja haluttuSivu
    public Sivu(int rivit, int haluttuSivu, String urlAlku, String urlLoppu) {
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
            this.edellinenSivu = urlAlku + (haluttuSivu-1) + urlLoppu;
        }
        this.seuraavaSivu = null;
        if(haluttuSivu<sivuja) {
            this.seuraavaSivu = urlAlku + (haluttuSivu+1) + urlLoppu;
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
    
}
