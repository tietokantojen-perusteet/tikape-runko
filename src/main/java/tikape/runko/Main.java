package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AiheDao;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Aihe;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:tsjtsfoorumi.db");
        database.init();

        AlueDao alueDao = new AlueDao(database);
        AiheDao aiheDao = new AiheDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());

        get("/alueet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("aiheet", aiheDao.alueenAiheet(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "aiheet");
        }, new ThymeleafTemplateEngine());
        
        get("/aiheet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            System.out.println("Aihe: " + req.params("id"));
            Aihe aihe = aiheDao.findOne(Integer.parseInt(req.params("id")));
            map.put("alue", alueDao.findOne(aihe.getAlue_id()));
            map.put("aihe", aihe);
            //map.put("viestit", null);
            // tämä kesken eli viestiDao tekemättä
            map.put("viestit", viestiDao.aiheenViestit(aihe.getAihe_id()));
            

            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());
    }
}