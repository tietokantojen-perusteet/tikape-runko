package tikape.runko;

public class AnnosRaakaAine {
    public Integer raakaAineId;
    public Integer annosId;
    Integer jarjestys;
    Double maara;
    String ohje;
    
    public AnnosRaakaAine(int raakaAineId, int annosId, int jarjestys, double maara, String ohje){
        this.raakaAineId = raakaAineId;
        this.annosId = annosId;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }
    
    public int getRaakaAineId(){
        return this.raakaAineId;
    }
    
    public int getAnnosId(){
        return this.annosId;
    }
    
    public int getJarjestys(){
        return this.jarjestys;
    }
    
    public Double getMaara(){
        return this.maara;
    }
    
    public String getOhje(){
        return this.ohje;
    }
    
    public void setRaakaAineId(int id){
        this.raakaAineId = id;
    }
    
    public void setAnnosId(int id){
        this.annosId = id;
    }
    
    public void setJarjestys(int jarjestys){
        this.jarjestys = jarjestys;
    }
    
    public void setMaara(double maara){
        this.maara = maara;
    }
    
    public void setOhje(String ohje){
        this.ohje = ohje;
    }
    
}
