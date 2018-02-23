
package tikape.runko;

import java.util.ArrayList;
import java.util.List;

public class Annos {
    private int id;
    private List<RaakaAine> lista;
    private String nimi;
    private String valmistusOhje;
    public Annos(int id, String nimi, String valmistusOhje) {
        this.id = id;
        this.nimi = nimi;
        this.lista = new ArrayList<>();
        // valmistus ohje on siis teksti tyyliin "Ensi kaataa kulhoon 2 dl maitoa, sitten...."
        this.valmistusOhje = valmistusOhje;
    }
    public int getId() {
        return this.id;
    }
    public String getNimi() {
        return this.nimi;
    }
    public List getRaakaAineLista() {
        return this.lista;
    }
    public String getValmistusOhje() {
        return this.valmistusOhje;
    }
}