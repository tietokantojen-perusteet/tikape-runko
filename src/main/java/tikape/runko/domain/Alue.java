package tikape.runko.domain;

public class Alue {
    private Integer alue_id;
    private String nimi;

    public Alue(Integer id, String nimi) {
        this.alue_id = id;
        this.nimi = nimi;
    }

    public Integer getId() {
        return alue_id;
    }

    public void setId(Integer id) {
        this.alue_id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
}
