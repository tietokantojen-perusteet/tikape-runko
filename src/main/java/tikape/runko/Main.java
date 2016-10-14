package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.thymeleaf.templateresolver.TemplateResolver;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.AlueDao;
import tikape.runko.database.KeskusteluDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Keskustelu;
import tikape.runko.domain.Viesti;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        Database database = new Database("jdbc:sqlite:keskustelualue.db");
        database.init();

        staticFileLocation("/public");

        AlueDao alueDao = new AlueDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database, alueDao);
        ViestiDao viestiDao = new ViestiDao(database, keskusteluDao);

        Spark.get("/", (req, res) -> {
            List<Alue> list = new ArrayList<>();
//            try {
//                System.out.println("Yritetään ajaa metodia findEtusivunalueet");
            list.addAll(alueDao.findEtusivunAlueet());
//            } catch (Throwable t) {
//                System.out.println(t.getMessage());
//            }

            HashMap map = new HashMap();
            map.put("alueet", list);
            res.type("text/html;charset=UTF-8");

            return new ModelAndView(map, "index");

        }, new ThymeleafTemplateEngine());

//        Spark.get("/alueet", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("alueet", alueDao.findAll());
//
//            return new ModelAndView(map, "index");
//        }, new ThymeleafTemplateEngine());

//        Spark.get("/alue/:id", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));
//
//            return new ModelAndView(map, "alue");
//        }, new ThymeleafTemplateEngine());
        Spark.get("alue/:id", (req, res) -> {
            List<Keskustelu> list = new ArrayList<>();
            int id = Integer.parseInt(req.params("id"));

            list.addAll(keskusteluDao.findAlueenKeskustelut(id));

//            for (Keskustelu keskustelu : keskusteluDao.findAll()) {
//                if (Integer.parseInt(req.params("id")) == keskustelu.getAlue_id()) {
//                    list.add(keskustelu);
//                }
//            }
            HashMap map = new HashMap();
            map.put("keskustelut", list);
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));
            return new ModelAndView(map, "alue");

        }, new ThymeleafTemplateEngine());

        Spark.post("alue/:id", (req, res) -> {
//            keskusteluDao.addOne();

            res.redirect("/alue/" + req.params("id"));
            return "Metodi ei vielä käytössä";

        });

        Spark.post("keskustelu/:id", (req, res) -> {
//            viestiDao.addOne();

            res.redirect("/keskustelu/" + req.params("id"));
            return "Turhaa tekstiä jota ei käytetä";

        });

        Spark.get("keskustelu/:id", (req, res) -> {

            Map map = new HashMap();
            map.put("keskustelu", keskusteluDao.findOne(Integer.parseInt(req.params("id"))));
            List<Viesti> list = new ArrayList();
            list.addAll(viestiDao.findKeskustelunViestit(Integer.parseInt(req.params("id"))));
//            List<Viesti> viestilista = viestiDao.findAll();
//            for (Viesti viesti : viestilista) {
//                if (viesti.getKeskustelu() == Integer.parseInt(req.params("id"))) {
//                    list.add(viesti);
//                }
//            }
            map.put("viestit", list);
            map.put("sivu", (Integer.parseInt(req.queryParams("page")) - 1) * 10);
            ModelAndView model = new ModelAndView(map, "Keskustelu");
            return model;
        }, new ThymeleafTemplateEngine());

    }
}
