
package tikape.runko.domain;

public class Keskustelualue {
        private Integer id;
        private String aihe;
    
    public Keskustelualue(Integer uusiId, String uusiAihe) {
        this.id = uusiId;
        this.aihe = uusiAihe;
    }
        
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAihe() {
        return this.aihe;
    }

    public void setAihe(String uusiAihe) {
        this.aihe = uusiAihe;
    }
}
