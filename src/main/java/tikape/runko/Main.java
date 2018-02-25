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
            // List<Annos>
            map.put("annokset", annokset.findAll());
            // List<RaakaAine>
            map.put("raakaaineet", raakaaineet.findAll());

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
            // fk:t määritelty "CASCADE ON DELETE", joten raaka-aineen poistaminen
            // poistaa myös raaka-aine-rivit ara:sta
            raakaaineet.delete(Integer.parseInt(req.params("id")));
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
            // List<RaakaAine>
            map.put("raakaaineet", raakaaineet.findAll());

            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine(templateResolver));
        
        // Annosten lisäys
        Spark.post("/reseptit", (req, res) -> {
            Annos annos = new Annos(-1, req.queryParams("nimi"));
            annokset.saveOrUpdate(annos);

            res.redirect("/reseptit");
            return "";
        });
        
        // Raaka-aineen lisäys annokseen
        Spark.post("/reseptit/lisaa", (req, res) -> {
            Annos annos = annokset.findOne(Integer.parseInt(req.queryParams("annos_id")));
            RaakaAine raakaaine = raakaaineet.findOne(Integer.parseInt(req.queryParams("raakaaine_id")));
            AnnosRaakaAine ara = new AnnosRaakaAine(
                    annos, raakaaine,
                    Integer.parseInt(req.queryParams("jarjestys")),
                    req.queryParams("maara"),
                    req.queryParams("ohje")
            );
            annosraakaaineet.saveOrUpdate(ara);

            res.redirect("/reseptit");
            return "";
        });

        // Yksittäisen annoksen näyttäminen
        get("/reseptit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Annos annos = annokset.findOne(Integer.parseInt(req.params("id")));

            // Annos
            map.put("annos", annos);
            // List<AnnosRaakaAine>
            map.put("annosraakaaineet", annosraakaaineet.annoksenRaakaAineet(annos));

            return new ModelAndView(map, "resepti");
        }, new ThymeleafTemplateEngine(templateResolver));
        
        // Annosten poisto
        get("/reseptit/:id/poista", (req, res) -> {
            // fk:t määritelty "CASCADE ON DELETE", joten annoksen poistaminen
            // poistaa myös annos-rivit ara:sta
            annokset.delete(Integer.parseInt(req.params("id")));

            res.redirect("/reseptit");
            return "";
        });
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
