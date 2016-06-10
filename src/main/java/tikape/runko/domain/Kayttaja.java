package tikape.runko.domain;

import java.util.*;

public class Kayttaja {
    private Integer id;
    private String tunnus;
    private String salasana;
    private String email;
    private Integer onko_super;
    private List<Viesti> viestit;
    
    public Kayttaja(Integer id, String tunnus, String salasana, String email) {
        this.id = id;
        this.tunnus = tunnus;
        this.salasana = salasana;
        this.email = email;
    }
    /* Superkäyttäjä(moderaattori) on 1, NULL tai 0 normaali käyttäjä */
    public Kayttaja(Integer id, String tunnus, String salasana, String email, Integer onko_super) {
        this.id = id;
        this.tunnus = tunnus;
        this.salasana = salasana;
        this.email = email;
        this.onko_super = onko_super;
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
