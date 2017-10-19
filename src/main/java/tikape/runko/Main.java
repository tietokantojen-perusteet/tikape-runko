package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothietietokanta.db");
        database.createTables();
        // SmoothieDAO smoothiet = new SmoothieDAO(database);
        // IngredientDAO raakaaineet = new IngredientDAO(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", "Tähän lista smoothieista");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/uusismoothie", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", "Tähän lista smoothieista");

            return new ModelAndView(map, "uusismoothie");
        }, new ThymeleafTemplateEngine());
        
        /*post("/uusismoothie", (req, res) -> {
            String smoothie = req.queryParams("nimi");
            smoothies.save(new Smoothie(null, nimi));
            res.redirect("/uusismoothie");
            return "";
        });*/
        
        get("/raakaaineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", "Tähän lista raaka-aineista");

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());
        
        /*post("/raakaaineet", (req, res) -> {
            String raakaaine = req.queryParams("nimi");
            raakaaineet.save(new Ingredient(null, nimi));
            res.redirect("/raakaaineet");
            return "";
        });*/

        /*get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());*/
    }
}
