package tikape.runko.domain;

import java.sql.Timestamp;

public class Alue {

    private Integer id;
    private String nimi;
    //private List<Keskustelu> keskustelut;
    private Integer viestienLkm;
    private SuomenAika viimeisinAika;

    public Alue(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
        //this.keskustelut = new ArrayList();
    }

    public Alue(Integer id, String nimi, Integer viestienLkm, Timestamp timestamp) {
        this.id = id;
        this.nimi = nimi;
        this.viestienLkm = viestienLkm;
        this.viimeisinAika = new SuomenAika(timestamp);
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

//    public List<Keskustelu> getKeskustelut() {
//        return keskustelut;
//    }
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
