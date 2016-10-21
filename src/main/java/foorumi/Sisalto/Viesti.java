package foorumi.Sisalto;

import java.sql.Timestamp;

public class Viesti {

    private int id;
    private String aihe_id;
    private String sisalto;
    private String aika;

    public Viesti(int i, String a, String s, String t) {
        id = i;
        aihe_id = a;
        sisalto = s;
        aika = t;
    }

    public Viesti(String aihe_id, String sisalto, String t) {
        // id = i;
        this.aihe_id = aihe_id;
        this.sisalto = sisalto;
        this.aika = t;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int i) {
        id = i;
    }

    public String getAihe() {
        return aihe_id;
    }

    public void setAihe(String a) {
        aihe_id = a;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String s) {
        sisalto = s;
    }

    public String getTimestamp() {
        return aika;
    }

    public void setTimestamp(String t) {
        aika = t;
    }

    public String toString() {
        return this.sisalto;
    }
    
    
}
