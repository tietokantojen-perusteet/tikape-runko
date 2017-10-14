package tikape.runko.database;

public class Annos {
    
    private String nimi;
    private Integer id;

    public Annos(Integer id, String nimi) {
        this.nimi = nimi;
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
}
