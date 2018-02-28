package tikape.runko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.JuomaDao;
import tikape.runko.database.JuomaRaakaAineDao;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Juoma;
import tikape.runko.domain.JuomaRaakaAine;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Connection conn = getConnection();
        
        Database database = new Database("jdbc:sqlite:data.db");
        RaakaAineDao raakaAineDao = new RaakaAineDao(conn);
        JuomaDao juomaDao = new JuomaDao(conn);
        JuomaRaakaAineDao juomaRaakaAineDao = new JuomaRaakaAineDao(conn);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Moi!");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/raakaAineetPoisto", (req, res) -> {
            HashMap map = new HashMap<>();
            List lista = raakaAineDao.findAll();
            map.put("lista", lista);
            
            return new ModelAndView(map, "raakaAineetPoisto");
        }, new ThymeleafTemplateEngine());
        
        post("/raakaAineetPoisto", (req, res) -> {
            int index = Integer.parseInt(req.queryParams("index"));
            RaakaAine ra = raakaAineDao.findOne(index);
            juomaRaakaAineDao.deleteRaakaAineenPerusteella(ra);
            raakaAineDao.delete(index);
            res.redirect("/raakaAineetPoisto");
            return "";
        });
        
        get("/juomatPoisto", (req, res) -> {
            HashMap map = new HashMap<>();
            List lista = juomaDao.findAll();
            map.put("lista", lista);
            
            return new ModelAndView(map, "juomatPoisto");
        }, new ThymeleafTemplateEngine());
        
        post("/juomatPoisto", (req, res) -> {
            int index = Integer.parseInt(req.queryParams("index"));
            Juoma ju = juomaDao.findOne(index);
            juomaRaakaAineDao.deleteJuomanPerusteella(ju);
            juomaDao.delete(index);
            res.redirect("/juomatPoisto");
            return "";
        });
        
        
        

        get("/juomat", (req, res) -> {
            HashMap map = new HashMap<>();
            List lista = juomaDao.findAll();
            map.put("lista", lista);

            return new ModelAndView(map, "juomat");
        }, new ThymeleafTemplateEngine());

        post("/juomat", (req, res) -> {

            String uudenJuomanNimi = req.queryParams("nimi");
            String vo = req.queryParams("valmistusohje");
            int uudenAineksenId = raakaAineDao.vapaaId();
            Juoma uusi = new Juoma(uudenAineksenId, uudenJuomanNimi, vo);
            Juoma a = juomaDao.saveOrUpdate(uusi);
            res.redirect("/juomat");
            return "";
        });
        
        get("/juomat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("juoma", juomaDao.findOne(Integer.parseInt(req.params("id"))));
            List lista = juomaRaakaAineDao.findJuomanPerusteella(juomaDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("lista", lista);
            List lista2 = raakaAineDao.findAll();
            map.put("lista2", lista2);
            
            
            return new ModelAndView(map, "juoma");
        }, new ThymeleafTemplateEngine());
        
        post("/juomat/:id", (req, res) -> {
            String raakaAineNimi = req.queryParams("uusiRaakaAine");
            String maara = req.queryParams("maara");
            String juomaNimi = juomaDao.findOne(Integer.parseInt("id")).getNimi();
            
            juomaRaakaAineDao.saveOrUpdate(new JuomaRaakaAine(juomaNimi, raakaAineNimi, maara));
            
            res.redirect("/");
            return "";
        });
        
        get("/lisays", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "lisays");
        }, new ThymeleafTemplateEngine());
        
        post("/lisays", (req, res) -> {
            String raakaAineNimi = req.queryParams("uusiRaakaAine");
            String maara = req.queryParams("maara");
            String juomaNimi = juomaDao.findOne(Integer.parseInt(req.queryParams("index"))).getNimi();
            
            juomaRaakaAineDao.saveOrUpdate(new JuomaRaakaAine(juomaNimi, raakaAineNimi, maara));
            
            res.redirect("/lisays");
            return "";
        });
        
        

        get("/raakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();

            List lista = raakaAineDao.findAll();
            map.put("lista", lista);

            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());
        
        post("/raakaAineet", (req, res) -> {

            String uudenAineksenNimi = req.queryParams("nimi");
            int uudenAineksenId = raakaAineDao.vapaaId();
            RaakaAine uusi = new RaakaAine(uudenAineksenId, uudenAineksenNimi);
            RaakaAine a = raakaAineDao.saveOrUpdate(uusi);
            res.redirect("/raakaAineet");
            return "";
        });
        
        get("/raakaAineet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAine", raakaAineDao.findOne(Integer.parseInt(req.params("id"))));
            List lista = juomaRaakaAineDao.findRaakaAineenPerusteella(raakaAineDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("lista", lista);
            return new ModelAndView(map, "raakaAine");
        }, new ThymeleafTemplateEngine());
        
        post("/raakaAineet/:id", (req, res) -> {
            int index = Integer.parseInt(req.params("id"));
            String t ="" + index; 
            res.redirect("/testiSivu/:" + t);
            return "";
        });
        
        get("/testiSivu/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            raakaAineDao.delete(Integer.parseInt("id"));

            return new ModelAndView(map, "testiSivu");
        }, new ThymeleafTemplateEngine());

    }

    public static Connection getConnection() throws Exception {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:data.db");
    }

}

