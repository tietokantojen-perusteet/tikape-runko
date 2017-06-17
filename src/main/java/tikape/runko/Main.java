package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.KetjuDao;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Alue;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:opiskelijat.db");
        database.init();
        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);
        
        Database database2 = new Database("jdbc:sqlite:foorumi.db");
        database2.init();

        AlueDao alueDao = new AlueDao(database2);
        KetjuDao ketjuDao = new KetjuDao(database2);
        ViestiDao viestiDao = new ViestiDao(database2);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        
        //Listaa kaikki alueet, toteutus html-filussa
        get("/alueet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());
        
        //Lisää uuden alueen
        post("/alue", (req, res) -> {
            if (!req.queryParams("alueNimi").isEmpty()) {
                alueDao.lisaaAlue(req.queryParams("alueNimi"));
            }
            res.redirect("/");
            return "";
        });
        
        //Listaa kaikki tietyn alueen alaisuudessa olevat ketjut ketjuun liittyvnä alue-fk:n mukaan
        get("/alueet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("ketjut", ketjuDao.findAllForAlueId(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "ketjut");
        }, new ThymeleafTemplateEngine());
        
        
        //Lisää uusi ketju ja viesti ketjuun
        post("/alueet/:id", (req, res) -> {
            
            ketjuDao.lisaaKetju(req.queryParams("ketjunNimi"), Integer.parseInt(req.params("id")));
            
            res.redirect("/alue");
            return "";
        });
        //LIsää uusi ketju ja viesti ketjuun
        
        //Listaa kaikki ketjuun liittyvät viestit viestiin liittyvän ketju-fk:n mukaan
        get("/alueet/:id/ketjut/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viestit", viestiDao.findAllForKetjuId(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine()); 

        //Vastaanottaa kirjoittajan viesti, ei toimi vielä oikein
        post("/ketjut/1", (req, res) -> {
            viestiDao.lisaaViesti(req.queryParams("kayttaja"), req.queryParams("viesti"), Integer.parseInt(req.params("1")));
            res.redirect("/alue");
            return "";
        });
        

        
        
        
        
        
        
        get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());
        

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
    }
}
