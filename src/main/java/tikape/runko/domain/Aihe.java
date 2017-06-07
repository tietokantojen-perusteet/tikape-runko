
package tikape.runko.domain;

// luokka foorumin aiheita varten
public class Aihe {
    private int aihe_id; // tietokannan aiheen pääaiavin
    private String otsikko; 
    private int viesteja; // tietokannassa olevien aiheseen kuuluvien viestien määrä
    private String viimeisin; // tietokannan viimeisin viesti aiheesta
    private int alue_id; // Alue taulun alue_id, jolle aihe kuuluu. Aihe taulun viiteavain

    // konstruktori, kun aihe haettu tietokannasta
    public Aihe(int aihe_id, String otsikko, int viesteja, String viimeisin, int alue_id) {
        this.aihe_id = aihe_id;
        this.otsikko = otsikko;
        this.viesteja = viesteja;
        this.viimeisin = viimeisin;
        this.alue_id = alue_id;
    }

    // konstruktori tilanteessa jossa aihetta ei ole vielä tietokannassa
    public Aihe(String otsikko, int alue_id) {
        this.aihe_id = 0;
        this.otsikko = otsikko;
        this.viesteja = 0;
        this.viimeisin = null;
        this.alue_id = alue_id;
    }

    public int getAihe_id() {
        return aihe_id;
    }

    public int getAlue_id() {
        return alue_id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public int getViesteja() {
        return viesteja;
    }

    public String getViimeisin() {
        return viimeisin;
    }
    
}


