
package tikape.runko.domain;

public class Aihe {
    private int aihe_id;
    private String otsikko;
    private int viesteja;
    private String viimeisin;
    private int alue_id;

    public Aihe(int aihe_id, String otsikko, int viesteja, String viimeisin, int alue_id) {
        this.aihe_id = aihe_id;
        this.otsikko = otsikko;
        this.viesteja = viesteja;
        this.viimeisin = viimeisin;
        this.alue_id = alue_id;
    }

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


