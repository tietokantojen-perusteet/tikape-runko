package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        // käytetään oletuksena paikallista sqlite-tietokantaa
        String jdbcOsoite = "jdbc:sqlite:tsjtsfoorumi.db";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 

        Database database = new Database(jdbcOsoite);
        database.init();

        // Tietokanta rajapinnat Alue, Aihe ja Viesti luokille
        AlueDao alueDao = new AlueDao(database);
        AiheDao aiheDao = new AiheDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        // Käyttäjä avaa pääsivun-> näytetään kaikki alueet
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());
            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());
        
        // tämä erilaisia testejä varten
        get("/test", (req, res) -> {
            res.redirect("/");
            return "";
        });
        
        // Lisätään uusi alue ja palataan pääsivulle
        // LISÄTTÄVÄ TOIMINTO JOKA TARKISTAA ENNEN LUONTIA ONKO SAMANNIMINEN ALUE JO LUOTU
        post("/alue", (req, res) -> {
            String alueSelite = req.queryParams("alue").trim();
            
            if (!alueSelite.isEmpty()) {
                Alue uusiAlue = new Alue(alueSelite);
                alueDao.create(uusiAlue);
            }
            res.redirect("/");
            return "";
        });

        // alueen ":id" valittu, ohjataan sivulle /alueet/:id/sivu/1
        get("/alueet/:id", (req, res) -> {
            res.redirect("/alueet/" + req.params("id") + "/sivu/1");
            return "";
        });     
        
        // näytetään alueen ":id" avaukset sivu ":s"
        // TOIMINNALLISUS TEKEMÄTTÄ, NÄYTTÄÄ NYT KAIKKI AIHEET 1-n
        get("/alueet/:id/sivu/:s", (req, res) -> {
            HashMap map = new HashMap<>();
            Alue alue = alueDao.findOne(Integer.parseInt(req.params("id")));
            if(alue==null) {
                map.put("virhekoodi", "Virheellinen aluevalinta. Aluetta " + req.params("id") + " ei ole tietokannassa.");
                map.put("uusisivu", "/");
                map.put("sivunnimi", "Pääsivulle");
                return new ModelAndView(map, "virhe");
            } else {
                // LISÄTTÄVÄ NIIN että vain 10 ekaa. sekä navigointi
                map.put("alue", alue);
                map.put("aiheet", aiheDao.alueenAiheet(Integer.parseInt(req.params("id"))));
                return new ModelAndView(map, "aiheet");
            }
        }, new ThymeleafTemplateEngine());
        
        // Lisätään uusi aihe alueeseen "id". luodaan aiheeseen myös ensimmäinen viesti
        // voiko tätä kutsua väärin? LISÄTÄÄN TARKISTUS JOS ALUE_ID väärin, merkkijonot liian pitkiä
        // virheestä voiaan ohjata virhe sivulle
        post("/aihe/:alue_id", (req, res) -> {
            String viesti = req.queryParams("viesti").trim();
            String nimimerkki = req.queryParams("nimimerkki").trim();
            String otsikko = req.queryParams("otsikko").trim();
            int alue_id = Integer.parseInt(req.params("alue_id"));
            
            if (!viesti.isEmpty()&&!nimimerkki.isEmpty()&&!otsikko.isEmpty()) {
                Aihe uusiAihe = new Aihe(otsikko, alue_id);
                Aihe luotuAihe = aiheDao.create(uusiAihe); 
                Viesti uusiViesti = new Viesti(luotuAihe.getAihe_id(), viesti, nimimerkki);
                viestiDao.create(uusiViesti);
            }
            // uusi aihe tulee listalla ensimmäiseksi joten siirrytään ko alueen listan alkuun.
            res.redirect("/alueet/"+alue_id);
            return "";
        });
        
        // aihe ":id" valittu, ohjataan sivulle /aiheet/:id/sivu/1
        get("/aiheet/:id", (req, res) -> {
            res.redirect("/aiheet/" + req.params("id") + "/sivu/1");
            return "";
        });     
        
        // näytetään aiheen ":id" viestit sivulta ":s"
        // TOIMINNALLISUUS KESKEN, LISÄTTÄVÄ SIVUJAON TEKEMINEN
        get("/aiheet/:id/sivu/:s", (req, res) -> {
            HashMap map = new HashMap<>();
            Aihe aihe = aiheDao.findOne(Integer.parseInt(req.params("id")));   
            if(aihe==null) {
                map.put("virhekoodi", "Virheellinen aihevalinta. Aihetta " + req.params("id") + " ei ole tietokannassa.");
                map.put("uusisivu", "/");
                map.put("sivunnimi", "Pääsivulle");                
                return new ModelAndView(map, "virhe"); 
            } else {
                // lisättävä että max 10 ekaa viestiä sekä navigointi
                map.put("alue", alueDao.findOne(aihe.getAlue_id()));
                map.put("aihe", aihe);
                map.put("viestit", viestiDao.aiheenViestit(aihe.getAihe_id()));  
                map.put("aloitusnumero", 1);
                return new ModelAndView(map, "viestit");
            }
        }, new ThymeleafTemplateEngine());
        
        // Lisätään uusi viesti ja palataan viestilista sivulle
        // voiko tätä kutsua väärin. Lisätään tarkistus että pituudet ok, aihe_id ok
        // ohjataan virhe sivulle
        post("/viesti/:aihe_id", (req, res) -> {
            String viesti = req.queryParams("viesti").trim();
            String nimimerkki = req.queryParams("nimimerkki").trim();
            int aihe_id = Integer.parseInt(req.params("aihe_id"));
            
            if (!viesti.isEmpty()&&!nimimerkki.isEmpty()) {
                Viesti uusiViesti = new Viesti(aihe_id, viesti, nimimerkki);
                viestiDao.create(uusiViesti);
            }
            // MUUTETTAVA NIIN ETTÄ SIIRRYTÄÄN VIESTI KETJUN VIIMEISELLE SIVULLE
            res.redirect("/aiheet/"+aihe_id);
            return "";
        });
    }
}