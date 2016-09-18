package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;

public class Main {

    public static void main(String[] args) throws Exception {
        //Tietokannan alustus
        Database database = new Database("jdbc:sqlite:keskustelualue.db");
        database.init();

        //Etusivu
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");
            //Tähän näkymä, jolla näytetään etusivu (kategoriat ja alakategoriat)
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        //Näytä viestiketju
        get("/thread/:threadId", (req, res) -> {
            int id = Integer.parseInt(req.params("threadId"));
            //Tähän näkymä, jossa näytetään viestiketju
            return "Viestiketjun id: " + id;
        });
        //Lähetä viestiketjuun uusi vastaus
        post("/thread/:threadId", (req, res) -> {
            int id = Integer.parseInt(req.params("threadId"));
            //Käsitellään tässä POST-pyynnön data ja lisätään tietokantaan
            return "Vastaus viestiketjuun, jolla id: " + id;
        });
        //Näytä alakategorian viestit:
        get("/subcategory/:subCategoryId", (req, res) -> {
            int id = Integer.parseInt(req.params("subCategoryId"));
            //Tähän näkymä, jossa näytetään alakategorian viestit
            return "Alakategorian id: " + id;
        });
        //Uuden viestiketjun lähettäminen:
        post("/subcategory/:subCategoryId", (req, res) -> {
            int id = Integer.parseInt(req.params("subCategoryId"));
            //Käsitellään tässä POST-pyynnön data ja lisätään tietokantaan
            return "Tällä käsitellään viestiketjun data alakategoriaan " + id + ".";
        });
        //Uuden viestiketjun luominen:
        get("/new/:subCategoryId", (req, res) -> {
            int id = Integer.parseInt(req.params("subCategoryId"));
            //Näytetään tässä lomake käyttäjälle
            return "Tällä luodaan uusi viestiketju alakategoriaan " + id + ".";
        });
    }
}
