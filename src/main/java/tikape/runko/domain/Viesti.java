
package tikape.runko.domain;

/**
 *
 * @author llmlks
 */
public class Viesti {
    
    private Integer id;
    private Integer avaus_id;
    private timestamp aika;
    private String sisalto;
    private String nimimerkki;

    public Viesti(Integer id, Integer avaus_id, timestamp aika, String sisalto, String nimimerkki) {
        this.id = id;
        this.avaus_id = avaus_id;
        this.aika = aika;
        this.sisalto = sisalto;
        this.nimimerkki = nimimerkki;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAvaus_id() {
        return avaus_id;
    }

    public void setAvaus_id(Integer avaus_id) {
        this.avaus_id = avaus_id;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public void setNimimerkki(String nimimerkki) {
        this.nimimerkki = nimimerkki;
    }
    
}
