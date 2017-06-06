package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AiheDao;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Aihe;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Viesti;

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
        //Database database = new Database("jdbc:sqlite:tsjtsfoorumi.db");
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
        
        // Lisätään uusi alue ja palataan pääsivulle
        post("/alue", (req, res) -> {
            String alueSelite = req.queryParams("alue").trim();
            
            if (!alueSelite.isEmpty()) {
                Alue uusiAlue = new Alue(alueSelite);
                alueDao.create(uusiAlue);
            }
            res.redirect("/");
            return "";
        });

        // näytetään alueen "id" avaukset
        get("/alueet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("aiheet", aiheDao.alueenAiheet(Integer.parseInt(req.params("id"))));
            return new ModelAndView(map, "aiheet");
        }, new ThymeleafTemplateEngine());
        
        // Lisätään uusi aihe alueeseen "id". luodaan aiheeseen myös ensimmäinen viesti
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
            res.redirect("/alueet/"+alue_id);
            return "";
        });
        
        // näytetään aiheen "id" viestit
        get("/aiheet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Aihe aihe = aiheDao.findOne(Integer.parseInt(req.params("id")));
            map.put("alue", alueDao.findOne(aihe.getAlue_id()));
            map.put("aihe", aihe);
            map.put("viestit", viestiDao.aiheenViestit(aihe.getAihe_id()));           
            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());
        
        // Lisätään uusi viesti ja palataan viestilista sivulle
        post("/viesti/:aihe_id", (req, res) -> {
            String viesti = req.queryParams("viesti").trim();
            String nimimerkki = req.queryParams("nimimerkki").trim();
            int aihe_id = Integer.parseInt(req.params("aihe_id"));
            
            if (!viesti.isEmpty()&&!nimimerkki.isEmpty()) {
                Viesti uusiViesti = new Viesti(aihe_id, viesti, nimimerkki);
                viestiDao.create(uusiViesti);
            }
            res.redirect("/aiheet/"+aihe_id);
            return "";
        });
    }
}