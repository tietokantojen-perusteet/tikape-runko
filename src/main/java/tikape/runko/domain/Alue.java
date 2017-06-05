package tikape.runko.domain;


public class Alue {
    private int alue_id;
    private String kuvaus;
    private int viesteja;
    private String viimeisin;

    public Alue(int alue_id, String kuvaus, int viesteja, String viimeisin) {
        this.alue_id = alue_id;
        this.kuvaus = kuvaus;
        this.viesteja = viesteja;
        this.viimeisin = viimeisin;
    }
 
    public Alue(String kuvaus) {
        this.alue_id = 0;
        this.kuvaus = kuvaus;
        this.viesteja = 0;
        this.viimeisin = null;
    }

    public int getAlue_id() {
        return alue_id;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public int getViesteja() {
        return viesteja;
    }

    public String getViimeisin() {
        return viimeisin;
    }
    
}

