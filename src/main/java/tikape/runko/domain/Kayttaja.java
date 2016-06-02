package tikape.runko.domain;

public class Kayttaja {
    private Integer id;
    private String tunnus;
    private String salasana;
    private String email;
    
    public Kayttaja(Integer id, String tunnus, String salasana, String email) {
        this.id = id;
        this.tunnus = tunnus;
        this.salasana = salasana;
        this.email = email;
    }
    public String toString() {
        return this.id + " " + this.tunnus + " " + this.salasana + " " + this.email;
    }
    public int getId() {
        return this.id;
    }
    public String getTunnus() {
        return this.tunnus;
    }
    public String getSalasana() {
        return this.salasana;
    }
    public String getEmail() {
        return this.email;
    }
}
