package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.Database;


public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("osoite");
        
        
        
        
        AnnosDao annosDao = new AnnosDao(database);
        
        Spark.get("/reseptit", (req, res) -> {
            List<Annos> annokset = new ArrayList<>();    
            List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
            
            annokset = annosDao.findAll();
            
            //Selvitetään seuraavaksi annoksiin sisältyvät raaka-aineet
            //Tallennetaan nämä HashMappiin, jossa avaimena on annos ja avaimeen liittyvänä
            //arvona lista, jossa on annokset raaka-aineet
            
            HashMap<Annos, List<RaakaAine>> raakaAineet = new HashMap<>();
            
            HashMap map = new HashMap<>();
            map.put("annokset", annokset);
            map.put("raakaAineet", raakaAineet);
            
            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/etusivu", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());
    }
}
