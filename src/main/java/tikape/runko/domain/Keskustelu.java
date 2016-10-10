package tikape.runko.domain;


public class Keskustelu {
    
    private Integer id;
    private Integer alue_id;
    private String otsikko;

    public Keskustelu(Integer id, Integer alue_id, String otsikko) {
        this.id = id;
        this.alue_id = alue_id;
        this.otsikko = otsikko;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlue_id() {
        return alue_id;
    }

    public void setAlue_id(Integer alue_id) {
        this.alue_id = alue_id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }
    
    
}
