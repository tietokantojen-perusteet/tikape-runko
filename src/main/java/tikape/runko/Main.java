package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;


public class Main {

    public static void main(String[] args) throws Exception {
        ArrayList<Annos> annokset = new ArrayList<>();
        ArrayList<RaakaAine> raakaAineet = new ArrayList<>();        
        ArrayList<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        
        
        
        Spark.get("/reseptit", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annokset);
            
            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/etusivu", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());
    }
}
