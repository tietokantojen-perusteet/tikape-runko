package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaaineDao;
import tikape.runko.database.AnnosDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothiearkisto.db");
        database.init();

        RaakaaineDao raakaaineDao = new RaakaaineDao(database);
        AnnosDao annosDao = new AnnosDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/annos/index", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annos_index", annosDao.findAll());

            return new ModelAndView(map, "annos_index");
        }, new ThymeleafTemplateEngine());

        get("/annos/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annos_show", annosDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "annos_show");
        }, new ThymeleafTemplateEngine());
        
        get("/raakaaine/index", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaine_index", annosDao.findAll());

            return new ModelAndView(map, "raakaaine_index");
        }, new ThymeleafTemplateEngine());

        get("/raakaaine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaine_show", annosDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "raakaaine_show");
        }, new ThymeleafTemplateEngine());
    }
}
