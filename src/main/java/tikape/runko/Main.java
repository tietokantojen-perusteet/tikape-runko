package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.UserDao;
import tikape.runko.domain.User;

public class Main {

    public static void main(String[] args) throws Exception {
        //Tietokannan alustus
        Database database = new Database("jdbc:sqlite:keskustelualue.db");
        database.init();

        UserDao userDao = new UserDao(database);

        //Oletusportti
        int appPort = 4567;
        if (System.getenv("PORT") != null) {
            appPort = Integer.parseInt(System.getenv("PORT"));
        }

        //Asetetaan oletusportti
        port(appPort);

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

        //Kirjaudu sisään
        post("/login", (req, res) -> {
            //Käsitellään tässä POST-pyynnön data
            //Käyttäjätunnus
            String username = req.queryParams("username").trim();
            //Salasana
            String password = req.queryParams("password");
            User u = userDao.findByUsername(username);
            //Jos käyttäjä löytyy tietokannasta
            if (u != null) {
                if (Auth.passwordMatches(password, u.getPasswordHash(), u.getSalt())) {
                    //Kirjaudu sisään. Asetetaan evästeet loggedIn = true ja userId = (USERID)
                    res.cookie("loggedIn", 1 + "", 63600);
                    res.cookie("userId", u.getId() + "", 3600);
                    res.redirect("/");
                    return "Kirjauduttu sisään.";
                } else {
                    //Väärä salasana!
                    return "Käyttäjätunnus tai salasana väärä.";
                }
            } else {
                //Käyttäjätunnusta ei ole olemassa!
                return "Käyttäjätunnus tai salasana väärä.";
            }

        });
    }
}
