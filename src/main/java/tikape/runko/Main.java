package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
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

            list.addAll(alueDao.findEtusivunAlueet());

            HashMap map = new HashMap();
            map.put("alueet", list);

            return new ModelAndView(map, "index");

        }, new ThymeleafTemplateEngine());

        Spark.get("/uusialue", (req, res) -> {
            HashMap map = new HashMap();
            return new ModelAndView(map, "uusialue");
        }, new ThymeleafTemplateEngine());

        Spark.post("/uusialue", (req, res) -> {
            if (req.queryParams("nimi").trim().isEmpty()) {
                return virheilmoitus("/uusialue", "Takaisin alueen lisäämiseen", "Alueen nimi ei voi olla tyhjä");
            }
            try {
                alueDao.lisaaAlue(req.queryParams("nimi"));
            } catch (Exception e) {
                return virheilmoitus("/uusialue", "Takaisin alueen lisäämiseen",
                        "Jokin meni pieleen :/ Alueen nimi saa olla korkeintaan 100 merkkiä pitkä.");
            }
            res.redirect("/");
            return "";
        });

        Spark.get("alue/:id", (req, res) -> {
            List<Keskustelu> list = new ArrayList<>();
            HashMap map = new HashMap();
            int id = 1;
            int kaikki = 0;

            try {
                kaikki = Integer.parseInt(req.queryParams("kaikki"));
            } catch (Exception e) {
            }
            try {
                id = Integer.parseInt(req.params("id"));
            } catch (Exception e) {
                res.redirect("/");
                return new ModelAndView(new HashMap(), "uusialue");
            }

            try {
                if (kaikki == 1) {
                    list.addAll(keskusteluDao.findAlueenKeskustelutKaikki(id));
                    if (list.size() > 10) {
                        map.put("kaikki", 1);
                    }
                } else {
                    list.addAll(keskusteluDao.findAlueenKeskustelutUusimmat(id));
                    if (list.size() == 11) {
                        list.remove(10);
                        map.put("kaikki", 0);
                    } else {
                        map.put("kaikki", -1);
                    }
                }

            } catch (Exception e) {
                res.redirect("/");
                return new ModelAndView(new HashMap(), "uusialue");
            }

            map.put("keskustelut", list);
            try {
                map.put("alue", alueDao.findOne(id));
            } catch (Exception e) {
                map.put("alue", new Alue(-5, "Virhe"));
                res.redirect("/");
                return new ModelAndView(new HashMap(), "uusialue");
            }

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        Spark.post("alue/:id", (req, res) -> {
            if (req.queryParams("sisalto").trim().isEmpty()
                    || req.queryParams("otsikko").trim().isEmpty()) {
                return virheilmoitus("/alue/" + req.params("id"), "Takaisin alueelle",
                        "Ei tyhjiä viestejä tai keskusteluja ilman otsikkoa, kiitos.");
            }
            int id = -1;
            try {
                id = Integer.parseInt(req.params("id"));
            } catch (Exception e) {

            }
            String nimi = req.queryParams("nimi");
            String otsikko = req.queryParams("otsikko");
            String sisalto = req.queryParams("sisalto");
            System.out.println("Valmistellaan keskustelun lisäystä...");
            try {
                keskusteluDao.lisaaKeskustelunavaus(id, otsikko, nimi, sisalto);
            } catch (Exception e) {
                return virheilmoitus("/alue/" + req.params("id"), "Takaisin keskustelun lisäämiseen",
                        "Jokin meni vikaan :/. Nimimerkki ja keskustelun otsikko saavat olla korkeintaan 100 merkkiä pitkiä");
            }

            res.redirect("/alue/" + id);
            return "OK";

        });

        Spark.post("keskustelu/:id", (req, res) -> {
//            int nykyinen_sivu = 1;

//            try {
//                nykyinen_sivu = Integer.parseInt(req.queryParams("nykyinen_sivu"));
//            } catch (Exception e) {
//            }
            if (req.queryParams("sisalto").trim().isEmpty()) {
                return virheilmoitus("/keskustelu/" + req.params("id"), "Takaisin viestin lisäämiseen",
                        "Ei tyhjiä viestejä, kiitos.");
            }

            try {
                viestiDao.lisaaViesti(Integer.parseInt(req.params("id")), req.queryParams("nimi"), req.queryParams("sisalto"));
            } catch (Exception e) {
                return "Jokin meni pieleen.. :(";
            }
            res.redirect("/keskustelu/" + req.params("id") + "?page=viimeinen");

            return "OK";
        });

        Spark.get("keskustelu/:id", (Request req, Response res) -> {
            int keskustelu_id = 1;

            try {
                keskustelu_id = Integer.parseInt(req.params("id"));
            } catch (Exception e) {
                res.redirect("/");
                return new ModelAndView(new HashMap(), "uusialue");
            }

            Map map = new HashMap();

            List<Viesti> list = new ArrayList();
            int sivunumero = 1;

            try {
                sivunumero = Integer.parseInt(req.queryParams("page"));
            } catch (Exception e) {
                if (req.queryParams("page") != null && req.queryParams("page").equals("viimeinen")) {
                    Integer keskustelunpituus = viestiDao.findKeskustelunViestitKaikki(keskustelu_id).size();
                    if (keskustelunpituus % viestienLkmSivulla == 0) {
                        sivunumero = keskustelunpituus / viestienLkmSivulla;
                    } else {
                        sivunumero = ((keskustelunpituus - (keskustelunpituus % viestienLkmSivulla)) / viestienLkmSivulla) + 1;
                    }
                }
            }

            try {
                list.addAll(viestiDao.findKeskustelunViestitSivullinenPlusYlimaaraiset(keskustelu_id, sivunumero, viestienLkmSivulla, 1));
            } catch (Exception e) {
                list.add(new Viesti(-5, null, "", "Jos näet tämän viestin, yritit hakea keskustelua jota ei ole olemassa"));
                res.redirect("/");
                return new ModelAndView(new HashMap(), "uusialue");
            }
            if (list.isEmpty()) {
                res.redirect("/keskustelu/" + keskustelu_id + "?page=1");
                return new ModelAndView(new HashMap(), "uusialue");
            }

            try {
                map.put("keskustelu", list.get(0).getKeskustelu());
                map.put("alue", list.get(0).getKeskustelu().getAlue());
//                map.put("keskustelu", keskusteluDao.findOne(keskustelu_id));
//                map.put("alue", alueDao.findOne(keskusteluDao.findOne(keskustelu_id).getAlue().getId()));
            } catch (Exception e) {
                map.put("keskustelu", new Keskustelu(-5, "Virhe"));
                map.put("alue", new Alue(-5, "Virhe"));
                res.redirect("/");
                return new ModelAndView(new HashMap(), "uusialue");
            }

            map.put("sivunviestit", (sivunumero - 1) * viestienLkmSivulla);
            map.put("page", sivunumero);
            if (list.size() == viestienLkmSivulla + 1) {
                list.remove(viestienLkmSivulla);
                map.put("nextpage", sivunumero + 1);
            }
            if (sivunumero > 1) {
                map.put("previouspage", sivunumero - 1);
            }

            map.put("viestit", list);
            ModelAndView model = new ModelAndView(map, "keskustelu");
            return model;
        }, new ThymeleafTemplateEngine());

    }

    public static String virheilmoitus(String paluuosoite, String paluulinkinTeksti, String virheteksti) {
        return "<!DOCTYPE html>"
                + "<head><meta charset='utf-8'/><title>Virhe lisäämisessä</title>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/styles.css\"/></head>"
                + "<body><h2>" + virheteksti + "</h2>"
                + "<a href='" + paluuosoite + "'>" + paluulinkinTeksti + "</a></body></html>";
    }
}
