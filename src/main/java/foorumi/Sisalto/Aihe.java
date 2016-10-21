package foorumi.Sisalto;

public class Aihe {

    private String nimi;
    private String alue;
    private int id;

    public Aihe(String n, String a, int id) {
        nimi = n;
        alue = a;
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String n) {
        nimi = n;
    }

    public String getAlue() {
        return alue;
    }

    public void setAlue(String a) {
        alue = a;
    }

    public int getId() {
        return this.id;
    }

//    public String getUrl() {
//        return this.nimi.replaceAll("ä", "ae").replaceAll("ö", "oe").replaceAll(" ", "_");
//    }
}
