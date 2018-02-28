/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Database db = new Database("jdbc:sqlite:data.db");
        RaakaAineDao raakaAineet = new RaakaAineDao(db);
        AnnosDao juomat = new AnnosDao(db);
        //sldkjfaölsdkfjaölsdkfjasöldkfajslödkfajsödlkfaödslkfajdölkfjaödlfk
        Spark.get("/kahvilaEtuSivu", (req, res) -> {
            
            HashMap map = new HashMap<>();
            map.put("t", "Meillä on seuraavat juomat");
            map.put("kahvilanJuomat", juomat.findAll());
            map.put("kahvilanRaakaAineet", raakaAineet.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.post("/autokauppa", (req, res) -> {
            res.redirect("/autokauppa");
            return "";
        });
        
    }
}
