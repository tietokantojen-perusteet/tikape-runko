package tikape.runko;

import tikape.runko.dao.AnnosDao;
import tikape.runko.dao.AnnosRaakaAineDao;
import tikape.runko.dao.RaakaAineDao;
import java.util.HashMap;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
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
        
        ITemplateResolver templateResolver = createDefaultTemplateResolver();
        
        // Pääsivu
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine(templateResolver));

        // Raaka-aineiden listaus
        get("/ainekset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaaineet.findAll());

            return new ModelAndView(map, "ainekset");
        }, new ThymeleafTemplateEngine(templateResolver));

        // Raaka-aineiden lisäys
        Spark.post("/ainekset", (req, res) -> {
            RaakaAine raakaaine = new RaakaAine(-1, req.queryParams("aine"));
            raakaaineet.saveOrUpdate(raakaaine);

            res.redirect("/ainekset");
            return "";
        });

        // Raaka-aineiden poisto
        get("/ainekset/:id/poista", (req, res) -> {
            // RaakaAineDao poistaa raaka-aineen sekä RaakaAineista että AnnosRaakaAineista
            raakaaineet.poistaRaakaAine(raakaaineet.findOne(Integer.parseInt(req.params("id"))), annokset, annosraakaaineet);
            // Mitä tehdään annoksille joissa on käytetty tätä raaka-ainetta?
            //Toistaiseksi annokset jäävät paikoilleen, mutta raaka-aineen viittaukset poistetaan niistä

            res.redirect("/ainekset");
            return "";
        });

        // Annosten listaaminen
        get("/reseptit", (req, res) -> {
            HashMap map = new HashMap<>();
            // List<Annos>
            map.put("annokset", annokset.findAll());
            map.put("raakaaineet", raakaaineet.findAll());

            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine(templateResolver));

        // Yksittäisen annoksen näyttäminen
        get("/reseptit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Annos annos = annokset.findOne(Integer.parseInt(req.params("id")));

            // Annos
            map.put("annos", annos);
            // List<AnnosRaakaAine>
            map.put("raakaaineet", annosraakaaineet.annoksenRaakaAineet(annos));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine(templateResolver));
    }

    private static ITemplateResolver createDefaultTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);

        templateResolver.setPrefix("templates/");

        templateResolver.setSuffix(".html");
        
        templateResolver.setCharacterEncoding("UTF-8");

        templateResolver.setCacheTTLMs(3600000L);
        return templateResolver;
    }
}
