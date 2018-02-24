package tikape.runko;

import tikape.runko.dao.AnnosDao;
import tikape.runko.dao.AnnosRaakaAineDao;
import tikape.runko.dao.RaakaAineDao;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.domain.*;
import tikape.runko.database.*;

public class Main {

    public static void main(String[] args) throws Exception {
        // heroku portti
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:reseptiarkisto.db");
        database.init();

        AnnosDao annokset = new AnnosDao(database);
        RaakaAineDao raakaaineet = new RaakaAineDao(database);
        AnnosRaakaAineDao annosraakaaineet = new AnnosRaakaAineDao(database, annokset, raakaaineet);

        // Pääsivu
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        // Raaka-aineiden listaus
        get("/raakaaineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaaineet.findAll());

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());
        
        // Raaka-aineiden lisäys
        Spark.post("/raakaaineet", (req, res) -> {
            RaakaAine raakaaine = new RaakaAine(-1, req.queryParams("nimi"));
            raakaaineet.saveOrUpdate(raakaaine);

            res.redirect("/raakaaineet");
            return "";
        });
        
        // Raaka-aineiden poisto
        get("/raakaaineet/:id/poista", (req, res) -> {
            // Poista eka tässä raaka-aineeseen viittaavat rivit arasta
            // Sitten poista raaka-aine raakaaineista
            raakaaineet.delete(Integer.parseInt(req.params("id")));
            // Mitä tehdään annoksille joissa on käytetty tätä raaka-ainetta?

            res.redirect("/raakaaineet");
            return "";
        });
        
        // Annosten listaaminen
        get("/annokset", (req, res) -> {
            HashMap map = new HashMap<>();
            // List<Annos>
            map.put("annokset", annokset.findAll());
            map.put("raakaaineet", raakaaineet.findAll());

            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
        
        // Yksittäisen annoksen näyttäminen
        get("/annokset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Annos annos = annokset.findOne(Integer.parseInt(req.params("id")));
            
            // Annos
            map.put("annos", annos);
            // List<AnnosRaakaAine>
            map.put("raakaaineet", annosraakaaineet.annoksenRaakaAineet(annos));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());
    }
}
