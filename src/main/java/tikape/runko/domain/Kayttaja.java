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
    /* Tällä kokeillaan kirjautumista */
    public Kayttaja(String tunnus, String salasana) {
        this.tunnus = tunnus;
        this.salasana = salasana;
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
    public int getID() {
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.tunnus);
        hash = 79 * hash + Objects.hashCode(this.salasana);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Kayttaja other = (Kayttaja) obj;
        if (!Objects.equals(this.tunnus, other.tunnus)) {
            return false;
        }
        if (!Objects.equals(this.salasana, other.salasana)) {
            return false;
        }
        return true;
    }
    
}
