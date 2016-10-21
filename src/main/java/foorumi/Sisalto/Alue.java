package foorumi.Sisalto;

import java.util.Collections;

public class Alue {

    private String nimi;
    private int id;

    public Alue(String n, int id) {
        nimi = n;
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String n) {
        nimi = n;
    }
    
    public int getId() {
        return this.id;
    }

//       public String getUrl() {
//        return this.nimi.replaceAll("ä", "ae").replaceAll("ö", "oe").replaceAll(" ", "_");
//           
//    }
}
