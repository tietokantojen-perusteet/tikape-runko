package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AihealueDao;
import tikape.runko.database.Database;
import tikape.runko.database.KetjuDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Aihealue;
import tikape.runko.domain.Ketju;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.init();

        AihealueDao aDao = new AihealueDao(database);
        KetjuDao kDao = new KetjuDao(database);
        ViestiDao vDao = new ViestiDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aihealueet", aDao.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            String aihe = req.queryParams("aihe");
            System.out.println(aihe);
            if (aihe.isEmpty()) {
                res.redirect("/");
                return null;
            }

            aDao.add(new Aihealue(aihe));
            res.redirect("/");

            return null;
        });

        get("Aihealue/:aihealue_id", (request, response) -> {

            HashMap map = new HashMap<>();
            map.put("ketjut", kDao.findAll());
            return new ModelAndView(map, "ketjut");
        }, new ThymeleafTemplateEngine());

    }
}
