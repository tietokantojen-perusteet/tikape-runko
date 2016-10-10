package tikape.runko.domain;

import java.util.ArrayList;
import java.util.List;

public class Alue {

    private Integer id;
    private String nimi;
    private List<Keskustelu> keskustelut;

    public Alue(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
        this.keskustelut = new ArrayList();
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

    public List<Keskustelu> getKeskustelut() {
        return keskustelut;
    }

}
