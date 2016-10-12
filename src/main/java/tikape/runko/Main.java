package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KeskustelualueDao;
import tikape.runko.database.KeskustelunavausDao;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.database.VastausDao;
import tikape.runko.domain.Keskustelualue;
import tikape.runko.domain.Keskustelunavaus;
import tikape.runko.domain.Vastaus;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi.db");
        KeskustelualueDao keskustelualuedao = new KeskustelualueDao(database);
        KeskustelunavausDao keskustelunavausdao = new KeskustelunavausDao(database);
        VastausDao vastausdao = new VastausDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("alueet", keskustelualuedao.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            keskustelualuedao.create(new Keskustelualue(req.queryParams("aihealue"), req.queryParams("kuvaus"), req.queryParams("perustaja")));
            
            res.redirect("/");
            return "";
        });
        
        get("/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer id = Integer.parseInt(req.params(":id"));

            map.put("alue", keskustelualuedao.findOne(id));
            map.put("avaukset", keskustelunavausdao.findAllInAlue(id));
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());
        
        post("/:id", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":id"));
            keskustelunavausdao.create(new Keskustelunavaus(keskustelualuedao.findOne(id), req.queryParams("otsikko"), req.queryParams("avaus"), req.queryParams("aloittaja")));
            
            res.redirect("/" + id);
            return "";
        });
        
        get("/:id/:idd", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer id = Integer.parseInt(req.params(":id"));
            Integer idd = Integer.parseInt(req.params(":idd"));

            map.put("alue", keskustelualuedao.findOne(id));
            map.put("avaus", keskustelunavausdao.findOne(idd));
            map.put("viestit", vastausdao.findAllInAvaus(idd));
            return new ModelAndView(map, "avaus");
        }, new ThymeleafTemplateEngine());
        
        post("/:id/:idd", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":id"));
            Integer idd = Integer.parseInt(req.params(":idd"));
            vastausdao.create(new Vastaus(keskustelunavausdao.findOne(idd), req.queryParams("teksti"), req.queryParams("kirjoittaja")));
            
            res.redirect("/" + id + "/" + idd);
            return "";
        });

//        get("/opiskelijat", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelijat", opiskelijaDao.findAll());
//
//            return new ModelAndView(map, "opiskelijat");
//        }, new ThymeleafTemplateEngine());
//
//        get("/opiskelijat/:id", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));
//
//            return new ModelAndView(map, "opiskelija");
//        }, new ThymeleafTemplateEngine());
    }
}
