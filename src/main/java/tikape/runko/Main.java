package tikape.runko;
    
import java.util.ArrayList;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KeskustelualueDao;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.domain.Keskustelualue;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi.db");
        KeskustelualueDao keskustelualuedao = new KeskustelualueDao(database);
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            ArrayList<String> alueet = new ArrayList<>();
            
            for (Keskustelualue alue : keskustelualuedao.findAll()) {
                alueet.add(alue.getAihealue());
            }
            
            map.put("alueet", alueet);
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

//        get("/opiskelijat", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelijat", opiskelijaDao.findAll());
//
//            return new ModelAndView(map, "opiskelijat");
//        }, new ThymeleafTemplateEngine());
//
//        get("/opiskelijat/:id", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));
//
//            return new ModelAndView(map, "opiskelija");
//        }, new ThymeleafTemplateEngine());
    }
}