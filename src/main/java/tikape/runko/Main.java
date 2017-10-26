package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.IngredientDAO;
import tikape.runko.database.SmoothieDAO;
import tikape.runko.domain.Ingredient;
import tikape.runko.domain.Smoothie;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothietietokanta.db");
        database.createTables();
        
        SmoothieDAO smoothieDAO = new SmoothieDAO(database);
        IngredientDAO ingredientDAO = new IngredientDAO(database);

        // Required for routing.
        staticFileLocation("/static/");

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", smoothieDAO.readAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/smoothiet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothie", smoothieDAO.read(Integer.parseInt(req.params("id"))));
            map.put("raakaaineet", smoothieDAO.read(Integer.parseInt(req.params("id"))).getIngredients());
            map.put("ohje", smoothieDAO.read(Integer.parseInt(req.params("id"))).getInstructions());
            
            return new ModelAndView(map, "smoothie");
        }, new ThymeleafTemplateEngine());
        
        get("/uusismoothie", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", smoothieDAO.readAll());
            map.put("raakaaineet", ingredientDAO.readAll());

            return new ModelAndView(map, "uusismoothie");
        }, new ThymeleafTemplateEngine());
        
        post("/uusismoothie", (req, res) -> {
            String nimi = req.queryParams("nimi");
            List<Ingredient> ingredients = new ArrayList<>();
            smoothieDAO.create(new Smoothie(null, nimi, null, ingredients));
            res.redirect("/uusismoothie");
            return "";
        });
        
        // Raaka-aineiden päivittäminen (vaiheessa, smoothieDAOon/ingredientDAOon pitäisi lisätä ominaisuuksia):
        /* 
        post("/uusismoothie", (req, res) -> {
            Smoothie muokattava = smoothieDAO.findByName(req.queryParams("smoothie");
            String lisattava = req.Params("raakaaine");
            Integer jarjestys = req.Params("jarjestys");
            String maara = req.Params("maara");
        

            smoothieDAO.update(muokattava);
            res.redirect("/uusismoothie");
            return "";
        });*/
        
        // Valmistusohjeen lisääminen (vaiheessa, smoothieDAOon pitäisi lisätä ominaisuuksia):
        /* 
        post("/uusismoothie", (req, res) -> {
            Smoothie muokattava = smoothieDAO.findByName(req.queryParams("smoothie");
            String ohje = req.Params("ohje");

            smoothieDAO.update(muokattava);
            res.redirect("/uusismoothie");
            return "";
        });*/
        
        get("/raakaaineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", ingredientDAO.readAll());

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());
        
        post("/raakaaineet", (req, res) -> {
            Ingredient raakaaine = new Ingredient(null, req.queryParams("raakaaine"));
            ingredientDAO.create(raakaaine);
            res.redirect("/raakaaineet");
            return "";
        });
        
        get("/raakaaineet/:id/poista", (req, res) -> {
            ingredientDAO.delete(Integer.parseInt(req.params(":id")));
            res.redirect("/raakaaineet");
            return "";
        });

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
