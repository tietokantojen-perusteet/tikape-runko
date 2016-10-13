package tikape.runko.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Keskustelu {

    private Integer id;
    private Alue alue;
    private String otsikko;
    //private List<Viesti> viestit;
    private Integer viestienLkm;
    private Timestamp viimeisinAika;

    public Keskustelu(Integer id, String otsikko) {
        this.id = id;
        this.otsikko = otsikko;
        //this.viestit = new ArrayList();
    }

    public Keskustelu(Integer id, Alue alue, String otsikko, Integer viestienLkm, Timestamp viimeisinAika) {
        this.id = id;
        this.alue = alue;
        this.otsikko = otsikko;
        this.viestienLkm = viestienLkm;
        this.viimeisinAika = viimeisinAika;
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

    public String getViimeisinAika() {
        if (this.viimeisinAika == null) {
            return "Ei viestejä";
        }
        return this.viimeisinAika.toString();
    }

    public void setViimeisinAika(Timestamp viimeisinAika) {
        this.viimeisinAika = viimeisinAika;
    }

}
