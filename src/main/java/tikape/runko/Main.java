package tikape.runko;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.Database;


public class Main {

    public static void main(String[] args) throws Exception {
        File tiedosto = new File("osoite","reseptit.db");
        System.out.println(tiedosto.getAbsolutePath());
        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());
        
        AnnosDao annosDao = new AnnosDao(database);
        RaakaAineDao aineDao = new RaakaAineDao(database);
        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database);
        
        Spark.get("/raakaAineet", (req, res) -> {
            
            
            HashMap map = new HashMap<>();
            map.put("aineet",aineDao.findAll());
             // TODO: Annoksien listaaminen ??
            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/reseptit", (req, res) -> {
            List<Annos> annokset = new ArrayList<>();    
            List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
            
            annokset = annosDao.findAll();
            
            //Selvitetään seuraavaksi annoksiin sisältyvät raaka-aineet
            //Tallennetaan nämä HashMappiin, jossa avaimena on annos ja avaimeen liittyvänä
            //arvona lista, jossa on annokset raaka-aineet
            
            HashMap<Annos, List<RaakaAine>> raakaAineet = annosRaakaAineDao.etsiRaakaAineet();
            
            HashMap map = new HashMap<>();
            map.put("annokset", annokset);
            map.put("raakaAineet", raakaAineet);
            
            annokset.stream().forEach(annos -> {
                List<RaakaAine> raakaAineLista = new ArrayList<>();
                raakaAineLista = raakaAineet.get(annos);
                map.put("raakaAineet"+annos.getNimi(), raakaAineLista);
            });
            
            System.out.println(annokset);
            System.out.println(raakaAineet);
            
            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/etusivu", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());
    }
}
