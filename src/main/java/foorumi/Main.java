package foorumi;

import foorumi.Sisalto.Aihe;
import foorumi.Sisalto.Alue;
import foorumi.Sisalto.Viesti;
import foorumi.collector.Aluekeraaja;
import foorumi.database.AiheDao;
import foorumi.database.AlueDao;
import foorumi.database.Database;
import foorumi.database.ViestiDao;
import java.util.*;
import java.sql.*;
import spark.*;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.setDebugMode(true);

        AiheDao aiheDao = new AiheDao(database, "jdbc:sqlite:foorumi.db");
        ViestiDao viestiDao = new ViestiDao(database, "jdbc:sqlite:foorumi.db");
        AlueDao alueDao = new AlueDao("jdbc:sqlite:foorumi.db");
        List<String> lista = viestiDao.findAikaViesteilleAiheesta("jääkiekko");
//
        Spark.get("/", (req, res) -> {
            res.redirect("/etusivu");
            return "ok";
        });

        //etusivu, eli alueet listattu
        get("/etusivu", (req, res) -> {

            List<Alue> alueet = alueDao.findAll();
            HashMap map = new HashMap<>();
            List<String> ajat = new ArrayList<>();
            List<String> viestit = new ArrayList<>();
            for (Alue i : alueet) {
                String aika = viestiDao.findUusinAikaAlueelta(i.getNimi());
                ajat.add(aika);
                String maara = viestiDao.laskeViestitAlueelta(i.getNimi());
                viestit.add(maara);
            }
            map.put("maara", "viestejä:");
            map.put("teksti", "Alueet");
            map.put("alueet", alueet);
            map.put("ajat", ajat);
            map.put("viestit", viestit);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        //tietty alue
        get("/alue/:id/:sivu", (req, res) -> {
            HashMap<String, Object> map = new HashMap<>();
            String id = req.params(":id");
            List<Aihe> aiheet = aiheDao.findWithId(id);
            List<String> ajat = new ArrayList<>();
            List<String> viestit = new ArrayList<>();
            for (Aihe i : aiheet) {
                String aika = viestiDao.findUusinAikaAiheelta(i.getNimi());
                ajat.add(aika);
                String maara = viestiDao.laskeViestitAiheelta(i.getNimi());

                viestit.add(maara);
            }
            String seuraavasivu = Integer.parseInt(req.params(":sivu")) + 1 + "";
            map.put("alue", req.params(":id"));
            map.put("seuraavasivu", seuraavasivu);
            map.put("edellinensivu", Integer.toString(Integer.parseInt(seuraavasivu) - 2));
            map.put("aiheet", aiheet);
            map.put("ajat", ajat);
            map.put("viestit", viestit);
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        //aiheen viestit;
        get("/aihe/:nimi/:sivu", (req, res) -> {
            HashMap<String, Object> map = new HashMap<>();
            List<Viesti> viestit = viestiDao.findWithAihe(req.params(":nimi"), req.params(":sivu"));        //haetaan nyt sivulta 2, voi muuttaa
            List<String> ajat = viestiDao.findAikaViesteilleAiheesta(req.params(":nimi"), req.params(":sivu"));     //haetaan nyt sivulta 2, voi muuttaaa
            String seuraavasivu = Integer.parseInt(req.params(":sivu")) + 1 + "";
            map.put("aihe", req.params(":nimi"));
            map.put("seuraavasivu", seuraavasivu);
            map.put("edellinensivu", Integer.toString(Integer.parseInt(seuraavasivu) - 2));
            map.put("viestit", viestit);
            map.put("ajat", ajat);
            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());

        //lisää viesti
        Spark.post("/aihe/:nimi/:sivu", (req, res) -> {
            viestiDao.lisaa(req.queryParams("viesti"), req.params(":nimi"), req.queryParams("nimimerkki"));

            String nimi = req.params(":nimi");

            HashMap<String, Object> map = new HashMap<>();
            List<Viesti> viestit = viestiDao.findWithAihe(req.params(":nimi"));
            map.put("viestit", viestit);
            res.redirect("/aihe/" + req.params(":nimi") + "/" + req.params(":sivu"));
            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());

        //lisää aihe
        post("/alue/:nimi/:sivu", (req, res) -> {
            Alue alue = alueDao.findOne(req.params(":nimi"));
            aiheDao.lisaa(req.queryParams("aihe"), alue.getId() + " ");

            res.redirect("/alue/" + req.params(":nimi") + "/" + req.params(":sivu"));
            return "jee";
        });

        post("/etusivu", (req, res) -> {     //luodaan uusi alue
            alueDao.lisaa(req.queryParams("alue"));

            res.redirect("/");
            return "jee";
        });
    }
}
