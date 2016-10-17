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

        String jdbcOsoite = "jdbc:sqlite:keskustelualue.db";
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        }

        Database database = new Database(jdbcOsoite);

        //Database database = new Database("jdbc:sqlite:keskustelualue.db");
        //database.init();
        staticFileLocation("/public");

        AlueDao alueDao = new AlueDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database, alueDao);
        ViestiDao viestiDao = new ViestiDao(database, keskusteluDao);

        int viestienLkmSivulla = 10;

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

        Spark.get("alue/:id", (req, res) -> {
            List<Keskustelu> list = new ArrayList<>();
            HashMap map = new HashMap();
            int id = Integer.parseInt(req.params("id"));
            try {
                if (Integer.parseInt(req.queryParams("kaikki")) == 1) {
                    list.addAll(keskusteluDao.findAlueenKeskustelutKaikki(id));
                    if (list.size() > 10) {
                        map.put("kaikki", 1);
                    }
                } else {
                    list.addAll(keskusteluDao.findAlueenKeskustelutUusimmat(id));
                    if (list.size() == 10) {
                        if (keskusteluDao.findAlueenKeskustelutKaikki(id).size() > 10) {
                            map.put("kaikki", 0);
                        }
                    } else {
                        map.put("kaikki", -1);
                    }
                }
            } catch (Exception e) {
                list.addAll(keskusteluDao.findAlueenKeskustelutUusimmat(id));
                if (list.size() == 10) {
                    if (keskusteluDao.findAlueenKeskustelutKaikki(id).size() > 10) {
                        map.put("kaikki", 0);
                    }
                } else {
                    map.put("kaikki", -1);
                }
            }

            map.put("keskustelut", list);
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        Spark.post("alue/:id", (req, res) -> {
            if (req.queryParams("sisalto").isEmpty()
                    || req.queryParams("otsikko").isEmpty()) {
                return "Ei tyhjiä viestejä tai keskusteluja ilman otsikkoa, kiitos. :(";
            }
            int id = Integer.parseInt(req.params("id"));
            String nimi = req.queryParams("nimi");
            String otsikko = req.queryParams("otsikko");
            String sisalto = req.queryParams("sisalto");
            System.out.println("Valmistellaan keskustelun lisäystä...");
            keskusteluDao.lisaaKeskustelunavaus(id, otsikko, nimi, sisalto);

            res.redirect("/alue/" + req.params("id"));
            return "OK";

        });

        Spark.post("keskustelu/:id", (req, res) -> {
            String nykyinen_sivu = req.queryParams("nykyinen_sivu");
            if (req.queryParams("sisalto").isEmpty()) {
                return "Ei tyhjiä viestejä, kiitos. :(";
            }

            try {
                viestiDao.lisaaViesti(Integer.parseInt(req.params("id")), req.queryParams("nimi"), req.queryParams("sisalto"));
            } catch (Exception e) {
                return "Jokin meni pieleen.. :(";
            }

            if (nykyinen_sivu != null && !nykyinen_sivu.isEmpty()) {
                res.redirect("/keskustelu/" + req.params("id") + "?page=" + req.queryParams("nykyinen_sivu"));
            } else {
                res.redirect("/keskustelu/" + req.params("id"));
            }
            return "OK";

        });

        Spark.get("keskustelu/:id", (req, res) -> {
            int keskustelu_id = 1;
            try {
                keskustelu_id = Integer.parseInt(req.params("id"));
            } catch (Exception e) {
                res.redirect("/");
            }

            Map map = new HashMap();
            map.put("keskustelu", keskusteluDao.findOne(keskustelu_id));
            map.put("alue", alueDao.findOne(keskusteluDao.findOne(keskustelu_id).getAlue().getId()));

            List<Viesti> list = new ArrayList();

            if (req.queryParams("page") != null && !req.queryParams("page").isEmpty()) {
                int sivunumero = 1;
                try {
                    sivunumero = Integer.parseInt(req.queryParams("page"));
                } catch (Exception e) {
                    res.redirect("/");
                }

                list.addAll(viestiDao.findKeskustelunViestitSivullinen(keskustelu_id, sivunumero, viestienLkmSivulla));
                map.put("sivunviestit", (sivunumero - 1) * 10);
                map.put("page", sivunumero);
                if (list.size() > 9) {
                    if (!viestiDao.findKeskustelunViestitSivullinen(keskustelu_id, sivunumero + 1, viestienLkmSivulla).isEmpty()) {
                        map.put("nextpage", sivunumero + 1);
                    }
                }
                if (sivunumero > 1) {
                    map.put("previouspage", sivunumero - 1);
                }

            } else {
                list.addAll(viestiDao.findKeskustelunViestitKaikki(keskustelu_id));
                if (list.size() > 9) {
                    res.redirect("/keskustelu/" + req.params("id") + "?page=1");
                }
                map.put("sivunviestit", 0);
            }
            if (list.isEmpty()) {
                res.redirect("/keskustelu/" + keskustelu_id);
            }
            map.put("viestit", list);

//            List<Viesti> viestilista = viestiDao.findAll();
//            for (Viesti viesti : viestilista) {
//                if (viesti.getKeskustelu() == Integer.parseInt(req.params("id"))) {
//                    list.add(viesti);
//                }
//            }
            ModelAndView model = new ModelAndView(map, "keskustelu");
            return model;
        }, new ThymeleafTemplateEngine());

    }
}
