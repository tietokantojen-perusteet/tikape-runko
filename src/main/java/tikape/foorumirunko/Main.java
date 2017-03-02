package tikape.foorumirunko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.foorumirunko.database.*;
import tikape.runko.database1.OpiskelijaDao;

public class Main {

    public static void main(String[] args) throws Exception {
        /* //herokuun siirtymiseen liittyvä portinhakusetti
         if (System.getenv("PORT") != null) {
         port(Integer.valueOf(System.getenv("PORT")));
         }
         */

        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.dropAllTables();
        database.init();

        KayttajaDao kayttajaDao = new KayttajaDao(database);
        ViestiDao viestiDao = new ViestiDao(database);
        AlueDao alueDao = new AlueDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/kayttajat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", kayttajaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", kayttajaDao.findOne(req.params("id")));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
    }
}
