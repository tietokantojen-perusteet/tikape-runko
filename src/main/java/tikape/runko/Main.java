package tikape.runko;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.CategoryDao;
import tikape.runko.database.Database;
import tikape.runko.database.SubCategoryDao;
import tikape.runko.database.UserDao;
import tikape.runko.domain.Category;
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
        Scanner sc = new Scanner(System.in);
        OUTER:
        while (true) {
            System.out.println("1) Listaa kategoriat ja niiden alakategoriat");
            System.out.println("2) Listaa viestiketjut alakategoriasta (TODO)");
            System.out.println("3) Lisää uusi kategoria");
            System.out.println("4) Lisää uusi alakategoria");
            System.out.println("5) Kirjoita uusi viestiketju (TODO)");
            System.out.println("6) Kirjoita uusi viesti viestiketjuun (TODO)");
            System.out.println("7) Lisää uusi käyttäjä tietokantaan");
            System.out.println("8) Listaa käyttäjät");
            System.out.println("exit Poistu ja käynnistä Web-sovellus");
            System.out.println("");
            System.out.print("> ");
            String komento = sc.nextLine();
            switch (komento) {
                case "exit":
                    break OUTER;
                case "1":
                    System.out.println("Listataan kategoriat ja niiden alakategoriat: ");
                    List<Category> categories = catDao.findAll();
                    for (Category cat : categories) {
                        System.out.println(cat.getCategoryId() + ": " + cat);
                        if (cat.getSubCategories().size() > 0) {
                            for (SubCategory subCat : cat.getSubCategories()) {
                                System.out.println("- " + subCat);
                            }
                        } else {
                            System.out.println("(Ei alakategorioita)");
                        }

                    }
                    break;
                case "2":

                    break;
                case "3":
                    System.out.print("Anna nimi: ");
                    String name = sc.nextLine();
                    Category c = new Category(-1, name);
                    catDao.add(c);
                    break;
                case "4":
                    System.out.print("Anna yläkategorian ID: ");
                    Integer categoryId = Integer.parseInt(sc.nextLine());
                    System.out.print("Anna nimi: ");
                    String subCategoryName = sc.nextLine();
                    System.out.print("Anna kuvaus: ");
                    String desc = sc.nextLine();
                    SubCategory subCategory = new SubCategory(categoryId, -1, subCategoryName).setDescription(desc);
                    subCatDao.add(subCategory);
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
                    System.out.print("Anna käyttäjätunnus: ");
                    String userName = sc.nextLine();
                    System.out.print("Anna salasana: ");
                    String passWd = sc.nextLine();
                    System.out.print("Anna salasana uudestaan: ");
                    String passWdAgain = sc.nextLine();
                    if (passWd.equals(passWdAgain)) {
                        System.out.println("Lisätään käyttäjä..");
                        userDao.add(userName, passWd);
                    }

                    break;
                case "8":
                    List<User> users = userDao.findAll();
                    for (User u : users) {
                        System.out.println(u);
                    }
                    break;
                default:
                    break;
            }
            System.out.println("");
        }

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
    }
}
