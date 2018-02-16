package tikape.runko;

public class AnnosRaakaAine {
    public Integer raakaAineId;
    public Integer annosId;
    Integer jarjestys;
    String maara;
    
    public AnnosRaakaAine(int raakaAineId, int annosId, int jarjestys, String maara){
        this.raakaAineId = raakaAineId;
        this.annosId = annosId;
        this.jarjestys = jarjestys;
        this.maara = maara;
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
    
    public String getMaara(){
        return this.maara;
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
    
    public void setMaara(String maara){
        this.maara = maara;
    }
    
}
