
package tikape.runko.domain;

public class AnnosRaakaAine implements Comparable<AnnosRaakaAine> {
    private Annos annos;
    private RaakaAine raakaAine;
    private int jarjestys;
    private String maara;
    private String ohje;

    public AnnosRaakaAine(Annos annos, RaakaAine raakaAine, int jarjestys, String maara, String ohje) {
        this.annos = annos;
        this.raakaAine = raakaAine;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Annos getAnnos() {
        return annos;
    }

    public void setAnnos(Annos annos) {
        this.annos = annos;
    }

    public RaakaAine getRaakaAine() {
        return raakaAine;
    }

    public void setRaakaAine(RaakaAine raakaAine) {
        this.raakaAine = raakaAine;
    }

    public int getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(int jarjestys) {
        this.jarjestys = jarjestys;
    }

    public String getMaara() {
        return maara;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }

    @Override
    public int compareTo(AnnosRaakaAine o) {
        return this.jarjestys - o.getJarjestys();
    }
}
