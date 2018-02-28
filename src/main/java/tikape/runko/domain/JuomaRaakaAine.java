
package tikape.runko.domain;

public class JuomaRaakaAine {
    private String juomaNimi;
    private String raakaAineNimi;
    private String maara;
    public JuomaRaakaAine (String juomaNimi, String raakaAineNimi, String maara) {
        this.juomaNimi = juomaNimi;
        this.raakaAineNimi = raakaAineNimi;
        this.maara = maara;
    }
    public String getJuoma() {
        return this.juomaNimi;
    }
    public String getRaakAine() {
        return this.raakaAineNimi;
    }
    public String getMaara() {
        return this.maara;
    }
    public String raakaAineidenEsitys() {
        return this.raakaAineNimi + "   " + this.maara;
    }
    public String juomienEsitys() {
        return this.juomaNimi;
    }
    
    public String toString() {
        return this.juomaNimi + this.raakaAineNimi + this.maara;
    }
}
