
package tikape.runko.domain;

public class Viesti {
    private int viesti_id;
    private int aihe_id;
    private String teksti;
    private String ajankohta;
    private String nimimerkki;

    public Viesti(int viesti_id, int aihe_id, String teksti, String ajankohta, String nimimerkki) {
        this.viesti_id = viesti_id;
        this.aihe_id = aihe_id;
        this.teksti = teksti;
        this.ajankohta = ajankohta;
        this.nimimerkki = nimimerkki;
    }
  
        public Viesti(int aihe_id, String teksti, String nimimerkki) {
        this.viesti_id = 0;
        this.aihe_id = aihe_id;
        this.teksti = teksti;
        this.nimimerkki = nimimerkki;
    }

    public int getAihe_id() {
        return aihe_id;
    }

    public String getAjankohta() {
        return ajankohta;
    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public String getTeksti() {
        return teksti;
    }

    public int getViesti_id() {
        return viesti_id;
    }
    
        
        
}
