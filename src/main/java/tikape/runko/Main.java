package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KayttajaDao;
import tikape.runko.database.KeskusteluDao;
import tikape.runko.domain.Kayttaja;
import tikape.runko.domain.Keskustelu;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:keskustelu.db");
        
        KayttajaDao kayttajaDao = new KayttajaDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database);
        
//        Kayttaja pekka = new Kayttaja(4,"seppo","salasana4","seppo@pekkala.fi");
//        kayttajaDao.luoKayttaja(pekka);
        
//        Keskustelu harleydavidson = new Keskustelu(1, "harley davidson", "Moottoripyörät");
//        keskusteluDao.luoKeskustelu(harleydavidson);
//        Keskustelu fenderstratocaster = new Keskustelu(2, "Fender Stratocaster", "Kitarat");
//        keskusteluDao.luoKeskustelu(fenderstratocaster);
        
        for (Kayttaja kayttaja : kayttajaDao.findAll()) {
            System.out.println(kayttaja);
        }
        
        for (Keskustelu keskustelu : keskusteluDao.findAll()){
            System.out.println(keskustelu);
        }
        
        System.out.println("hello hello");
        System.out.println("jepjep");
        
    }
}
        
//        Database database = new Database("jdbc:sqlite:opiskelija.db");
//        database.init();
//
//        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);
//        
//        

//        get("/", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("viesti", "tervehdys");
//
//            return new ModelAndView(map, "index");
//        }, new ThymeleafTemplateEngine());
//
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

