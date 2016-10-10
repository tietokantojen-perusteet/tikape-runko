package tikape.runko.domain;

import java.util.ArrayList;
import java.util.List;

public class Keskustelu {

    private Integer id;
    private Alue alue;
    private String otsikko;
    private List<Viesti> viestit;

    public Keskustelu(Integer id, String otsikko) {
        this.id = id;
        this.alue = alue;
        this.otsikko = otsikko;
        this.viestit = new ArrayList();
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

}
