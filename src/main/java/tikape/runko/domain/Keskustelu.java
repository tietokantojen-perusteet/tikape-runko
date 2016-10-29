package tikape.runko.domain;

import java.sql.Timestamp;

public class Keskustelu {

    private Integer id;
    private Alue alue;
    private String otsikko;
    //private List<Viesti> viestit;
    private Integer viestienLkm;
    private SuomenAika viimeisinAika;

    public Keskustelu(Integer id, String otsikko) {
        this.id = id;
        this.otsikko = otsikko;
        //this.viestit = new ArrayList();
    }

    public Keskustelu(Integer id, Alue alue, String otsikko, Integer viestienLkm, Timestamp timestamp) {
        this.id = id;
        this.alue = alue;
        this.otsikko = otsikko;
        this.viestienLkm = viestienLkm;
        this.viimeisinAika = new SuomenAika(timestamp);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Alue getAlue() {
        return alue;
    }

    public void setAlue(Alue alue) {
        this.alue = alue;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public Integer getViestienLkm() {
        return viestienLkm;
    }

    public void setViestienLkm(Integer viestienLkm) {
        this.viestienLkm = viestienLkm;
    }

    public SuomenAika getViimeisinAika() {
        return viimeisinAika;
    }

    public void setViimeisinAika(Timestamp timestamp) {
        this.viimeisinAika = new SuomenAika(timestamp);
    }

}
