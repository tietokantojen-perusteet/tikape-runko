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
import tikape.runko.domain.Alue;

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
            map.put("viestimaarat", viestimaarat);
            map.put("viimeisimmat", viimeisimmat);
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aihelista", aiheDao.findAlueesta(Integer.parseInt(req.params(":id"))));
            return new ModelAndView(map, "aiheet");
        }, new ThymeleafTemplateEngine());
    }
}
