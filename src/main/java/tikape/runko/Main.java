package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AiheDao;
import tikape.runko.database.Database;
import tikape.runko.database.AlueDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Aihe;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Viesti;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.init();

        AlueDao alueDao = new AlueDao(database);
        AiheDao aiheDao = new AiheDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            List<String> viimeisimmat = new ArrayList<>();
            for (Alue alue : alueDao.findAll()) {
                viimeisimmat.add("" + alueDao.getViimeisin(alue.getId()));
            }

            List<String> viestimaarat = new ArrayList<>();
            for (Alue alue : alueDao.findAll()) {
                viestimaarat.add("" + alueDao.getLukumaara(alue.getId()));
            }

            map.put("alueet", alueDao.findAll());
            map.put("viestimäärät", viestimaarat);
            map.put("viimeisimmät", viimeisimmat);
            return new ModelAndView(map, "indexi");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            alueDao.create(req.queryParams("name"));
            res.redirect("/");
            return "";
        });

        post("/alue/:id", (req, res) -> {
            aiheDao.create(req.params(":id"), req.queryParams("name"));
            res.redirect("/alue/" + req.params(":id"));
            return "";
        });

        post("/aihe/:id", (req, res) -> {
            viestiDao.create(req.params(":id"), req.queryParams("name"), req.queryParams("viesti"));
            res.redirect("/aihe/" + req.params(":id"));
            return "";
        });

        get("/alue/:id", (req, res) -> {
            HashMap map = new HashMap<>();

            List<Aihe> aiheet = aiheDao.findViimeisimmatKymmenenAihetta(Integer.parseInt(req.params(":id")));
            List<String> viimeisimmat = new ArrayList<>();

            for (Aihe aihe : aiheet) {
                viimeisimmat.add("" + aiheDao.getViimeisin(aihe.getId()));
            }

            List<String> viestimaarat = new ArrayList<>();
            for (Aihe aihe : aiheet) {
                viestimaarat.add("" + aiheDao.getLukumaara(aihe.getId()));
            }

            String alueid = req.params(":id");
            Alue alue = alueDao.findOne(Integer.parseInt(alueid));
            String aluenimi = alue.getNimi();

            map.put("viestimaarat", viestimaarat);
            map.put("viimeisimmat", viimeisimmat);
            map.put("alueid", alueid);
            map.put("aluenimi", aluenimi);
            map.put("aihelista", aiheet);
            return new ModelAndView(map, "aiheet_taulukko");
        }, new ThymeleafTemplateEngine());

        get("/aihe/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Viesti> viestilista = viestiDao.findKymmenenViimeisintaViestia((Integer.parseInt(req.params(":id"))), 1);
            String aiheid = req.params(":id");
            String alueid = aiheDao.findAlueid(aiheid);
            Aihe aihe = aiheDao.findOne(Integer.parseInt(aiheid));
            String aihenimi = aihe.getNimi();
            Alue alue = alueDao.findOne(Integer.parseInt(alueid));
            String aluenimi = alue.getNimi();

            map.put("alueid", alueid);
            map.put("aiheid", aiheid);
            map.put("aihenimi", aihenimi);
            map.put("aluenimi", aluenimi);
            map.put("viestilista", viestilista);
            return new ModelAndView(map, "aihe_taulukko");
        }, new ThymeleafTemplateEngine());

        //Sivunumerolliset hillitsemään ja hallitsemaan aihe- ja viestitulvaa
        //Ei toimi vielä kunnolla!
        get("alue/:id/aihe/:id/sivu/:sivunro", (req, res) -> {
            HashMap map = new HashMap<>();
            String aiheid = req.params(":id");
            String alueid = aiheDao.findAlueid(aiheid);
            Aihe aihe = aiheDao.findOne(Integer.parseInt(aiheid));
            String aihenimi = aihe.getNimi();
            Alue alue = alueDao.findOne(Integer.parseInt(alueid));
            String aluenimi = alue.getNimi();
            List<Viesti> viestilista = viestiDao.findKymmenenViimeisintaViestia((Integer.parseInt(aiheid)), (Integer.parseInt(req.params(":sivunro"))));

            map.put("alueid", alueid);
            map.put("aiheid", aiheid);
            map.put("aihenimi", aihenimi);
            map.put("aluenimi", aluenimi);
            map.put("viestilista", viestilista);
            map.put("seuraavasivu", Integer.toString(Integer.parseInt(req.params(":sivunro")) + 1));
            map.put("edellinensivu", Integer.toString(Integer.parseInt(req.params(":sivunro")) - 1));
            return new ModelAndView(map, "aihe_taulukko");
        }, new ThymeleafTemplateEngine());

        // viestien lähettäminen ja sivunumerot
        post("/aihe/:aiheid/sivu/:sivunro", (req, res) -> {
            viestiDao.create(req.params(":id"), req.queryParams("name"), req.queryParams("viesti"));
            res.redirect("/aihe/" + Integer.parseInt(req.params(":aiheid")) + "/sivu/" + Integer.parseInt(req.params(":sivunro")));

            return "ok";
        });

        // Tässä aiheen poistamiseen
        post("/aihe/poista/:id", (req, res) -> {
            String alue_id = aiheDao.findAlueid(req.params(":id"));

            aiheDao.poista(req.params(":id"));
            res.redirect("/alue/" + alue_id);
            return "";

        });
        // Tässä viestin poistamiseen
        post("/viesti/poista/:id", (req, res) -> {
            String aihe_id = viestiDao.getAiheId(req.params(":id"));

            viestiDao.poista(req.params(":id"));
            res.redirect("/aihe/" + aihe_id);
            return "";
        });

    }
}
