package tikape.runko;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.CategoryDao;
import tikape.runko.database.Database;
import tikape.runko.database.MessageThreadDao;
import tikape.runko.database.SubCategoryDao;
import tikape.runko.database.UserDao;
import tikape.runko.domain.Category;
import tikape.runko.domain.Message;
import tikape.runko.domain.MessageThread;
import tikape.runko.domain.SubCategory;
import tikape.runko.domain.User;

public class Main {

    public static void main(String[] args) throws Exception {
        //Tietokannan alustus
        Database database = new Database("jdbc:sqlite:keskustelualue.db");
        database.init();

        UserDao userDao = new UserDao(database);
        CategoryDao catDao = new CategoryDao(database);
        SubCategoryDao subCatDao = new SubCategoryDao(database);
        MessageThreadDao msgDao = new MessageThreadDao(database);
        Scanner sc = new Scanner(System.in);

        //Tekstikäyttöliittymän alustus
        TextUi textUi = new TextUi(sc, userDao, catDao, subCatDao, msgDao);
        //Näytä tekstikäyttöliittymä
//        textUi.show();

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
            //Tähän näkymä, jolla näytetään etusivu (kategoriat ja alakategoriat)
            //Haetaan kategoriat
            List<Category> categories = catDao.findAll();
            map.put("kategoriat", categories);
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
                    res.redirect("/login");
                    return "Käyttäjätunnus tai salasana väärä.";
                }
            } else {
                //Käyttäjätunnusta ei ole olemassa!
                res.redirect("/login");
                return "Käyttäjätunnus tai salasana väärä.";
            }

        });

        //Kirjautumissivu
        get("/login", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());
        //Rekisteröitymissivu
        get("/register", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "register");
        }, new ThymeleafTemplateEngine());
        //Uloskirjautuminen
        get("/logout", (req, res) -> {
            res.cookie("loggedIn", "0", 0);
            res.cookie("userId", "0", 0);
            res.redirect("/");
            return "";
        });
    }
}
