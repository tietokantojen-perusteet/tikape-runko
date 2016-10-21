package tikape.runko.domain;

public class Aihe {

    private Integer id;
    private String nimi;
    private Integer alue_id;

    public Aihe(Integer id, String nimi, Integer alue_id) {
        this.alue_id = alue_id;
        this.id = id;
        this.nimi = nimi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * @return the alue
     */
    public Integer getAlue() {
        return alue_id;
    }

    /**
     * @param alue the alue to set
     */
    public void setAlue(Integer alue_id) {
        this.alue_id = alue_id;
    }

}
