package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AiheDao;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.OpiskelijaDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:tsjtsfoorumi.db");
        database.init();

        AlueDao alueDao = new AlueDao(database);
        AiheDao aiheDao = new AiheDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());

        get("/alueet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            System.out.println("Alue: " + req.params("id"));
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("aiheet", aiheDao.alueenAiheet(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "aiheet");
        }, new ThymeleafTemplateEngine());
    }
}