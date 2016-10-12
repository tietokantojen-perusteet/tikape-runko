package tikape.runko.domain;

public class Vastaus {
    private Integer id;
    private Keskustelunavaus avaus;
    private String teksti;
    private String ajankohta;
    private String kirjoittaja;
    
    //Konstruktorit
    
    public Vastaus() {
    }
    
    public Vastaus (Integer id, Keskustelunavaus avaus, String teksti, String ajankohta, String kirjoittaja) {
        this.id = id;
        this.avaus = avaus;
        this.teksti = teksti;
        this.ajankohta = ajankohta;
        this.kirjoittaja = kirjoittaja;
    }
    
    public Vastaus (Keskustelunavaus avaus, String teksti, String kirjoittaja) {
        this(null, avaus, teksti, null, kirjoittaja);
    }
    
    //Getterit
    
    public Keskustelunavaus getAvaus() {
        return this.avaus;
    }
    
    public String getKirjoittaja() {
        return this.kirjoittaja;
    }
    
    public String getTeksti() {
        return this.teksti;
    }
    
    public String getAjankohta() {
        return this.ajankohta;
    }

    public Integer getId() {
        return id;
    }
    
    //Setterit
    
    public void setAvaus(Keskustelunavaus avaus) {
        this.avaus = avaus;
    }
    
    public void setKirjoittaja(String kirjoittaja) {
        this.kirjoittaja = kirjoittaja;
    }
    
    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }
    
    public void setAjankohta(String ajankohta) {
        this.ajankohta = ajankohta;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}