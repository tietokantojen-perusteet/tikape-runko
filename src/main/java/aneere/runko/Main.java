package aneere.runko;

import static spark.Spark.*;
import aneere.runko.database.Database;
import aneere.runko.database.KeskusteluDao;


public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        // käytetään oletuksena paikallista sqlite-tietokantaa
        String jdbcOsoite = "jdbc:sqlite:keskustelu.db";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 

        Database db = new Database(jdbcOsoite);
        KeskusteluDao keda = new KeskusteluDao(db);
        Kayttoliittyma kali = new Kayttoliittyma(db, keda);
        kali.suorita();       
    }
}
        
