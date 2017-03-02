package tikape.runko.domain;

public class Aihealue {
    
    private final int id;
    private final String aihe;
    private int koko = 0;
    private String viimeisin = "-";
    
    public Aihealue(String aihe) {
        this.id = -1;
        this.aihe = aihe;
    }
    
    public Aihealue(int id, String aihe) {
        this.id = id;
        this.aihe = aihe;
    }
    
    public int getId() {
        return id;
    }
    
    public String getAihe() {
        return aihe;
    }
    
    public int getKoko() {
        return koko;
    }
    
    public void setKoko(int koko) {
        this.koko = koko;
    }
    
    public String getViimeisin() {
        return viimeisin;
    }
    
    public void setViimeisin(String viimeisin) {
        this.viimeisin = viimeisin;
    }
}