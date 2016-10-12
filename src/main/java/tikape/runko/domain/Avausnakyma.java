package tikape.runko.domain;

public class Avausnakyma {
    private int id;
    private String aihealue;
    private int viestejaYhteensa;
    private String viimeisinViesti;

    public Avausnakyma(int id, String aihealue, int viestejaYhteensa, String viimeisinViesti) {
        this.id = id;
        this.aihealue = aihealue;
        this.viestejaYhteensa = viestejaYhteensa;
        this.viimeisinViesti = viimeisinViesti;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAihealue() {
        return aihealue;
    }

    public void setAihealue(String aihealue) {
        this.aihealue = aihealue;
    }

    public int getViestejaYhteensa() {
        return viestejaYhteensa;
    }

    public void setViestejaYhteensa(int viestejaYhteensa) {
        this.viestejaYhteensa = viestejaYhteensa;
    }

    public String getViimeisinViesti() {
        return viimeisinViesti;
    }

    public void setViimeisinViesti(String viimeisinViesti) {
        this.viimeisinViesti = viimeisinViesti;
    }
}
