package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner lukija = new Scanner(System.in);
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        // käytetään oletuksena paikallista sqlite-tietokantaa
        String jdbcOsoite = "jdbc:sqlite:tsjtsfoorumi.db";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 

        Database database = new Database(jdbcOsoite);
        database.init();

        // Tietokanta rajapinnat Alue, Aihe ja Viesti luokille
        AlueDao alueDao = new AlueDao(database);
        AiheDao aiheDao = new AiheDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        // Käyttäjä avaa pääsivun-> näytetään kaikki alueet
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());
            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());
                 
        // Lisätään uusi alue ja palataan pääsivulle
        post("/alue", (req, res) -> {
            String alueSelite = req.queryParams("alue").trim();           
            if (!alueSelite.isEmpty()&&alueSelite.length()<51) {
                for(Alue alue : alueDao.findAll()) {
                    if(alue.getKuvaus().equals(alueSelite)) {
                        res.redirect("/virhe/alue/1");
                        return "";    
                    }
                }
                Alue uusiAlue = new Alue(alueSelite);
                alueDao.create(uusiAlue);
            }
            res.redirect("/");
            return "";
        });

        // alueen ":id" valittu, ohjataan sivulle /alueet/:id/sivu/1
        get("/alueet/:id", (req, res) -> {
            res.redirect("/alueet/" + req.params("id") + "/sivu/1");
            return "";
        });     
        
        // näytetään alueen ":id" avaukset sivu ":s"
        get("/alueet/:id/sivu/:s", (req, res) -> {
            HashMap map = new HashMap<>();
            Alue alue = alueDao.findOne(Integer.parseInt(req.params("id")));
            if(alue==null) {
                map.put("virhekoodi", "Virheellinen aluevalinta. Aluetta " + req.params("id") + " ei ole tietokannassa.");
                map.put("uusisivu", "/");
                map.put("sivunnimi", "Pääsivulle");
                return new ModelAndView(map, "virhe");
            } else {
                map.put("alue", alue);
                ArrayList<Aihe> kaikkiAiheet = (ArrayList) aiheDao.alueenAiheet(Integer.parseInt(req.params("id")));
                int haluttuSivu = Integer.parseInt(req.params("s"));
                Sivu sivut = new Sivu(kaikkiAiheet.size(), haluttuSivu, "location.href='/alueet/" + alue.getAlue_id() + "/sivu/", "'");
                map.put("aiheet", kaikkiAiheet.subList(sivut.getEkaRivi(), sivut.getVikaRivi()+1));
                map.put("sivut", sivut);
                return new ModelAndView(map, "aiheet");
            }
        }, new ThymeleafTemplateEngine());
        
        // Lisätään uusi aihe alueeseen "id". luodaan aiheeseen myös ensimmäinen viesti
        post("/aihe/:alue_id", (req, res) -> {
            String viesti = req.queryParams("viesti").trim();
            String nimimerkki = req.queryParams("nimimerkki").trim();
            String otsikko = req.queryParams("otsikko").trim();
            int alue_id = Integer.parseInt(req.params("alue_id"));
            
            if (!viesti.isEmpty()&&!nimimerkki.isEmpty()&&!otsikko.isEmpty()&&viesti.length()<501&&nimimerkki.length()<26&&otsikko.length()<51&&alueDao.findOne(alue_id)!=null) {
                Aihe uusiAihe = new Aihe(otsikko, alue_id);
                Aihe luotuAihe = aiheDao.create(uusiAihe); 
                Viesti uusiViesti = new Viesti(luotuAihe.getAihe_id(), viesti, nimimerkki);
                viestiDao.create(uusiViesti);
            } else {
                res.redirect("/virhe/aihe/"+alue_id);
                return "";              
            }
            // uusi aihe tulee listalla ensimmäiseksi joten siirrytään ko alueen listan alkuun.
            res.redirect("/alueet/"+alue_id);
            return "";
        });
        
        // aihe ":id" valittu, ohjataan sivulle /aiheet/:id/sivu/1
        get("/aiheet/:id", (req, res) -> {
            res.redirect("/aiheet/" + req.params("id") + "/sivu/1");
            return "";
        });     
        
        // näytetään aiheen ":id" viestit sivulta ":s"
        get("/aiheet/:id/sivu/:s", (req, res) -> {
            HashMap map = new HashMap<>();
            Aihe aihe = aiheDao.findOne(Integer.parseInt(req.params("id")));   
            if(aihe==null) {
                map.put("virhekoodi", "Virheellinen aihevalinta. Aihetta " + req.params("id") + " ei ole tietokannassa.");
                map.put("uusisivu", "/");
                map.put("sivunnimi", "Pääsivulle");                
                return new ModelAndView(map, "virhe"); 
            } else {
                map.put("alue", alueDao.findOne(aihe.getAlue_id()));
                map.put("aihe", aihe); 
                ArrayList<Aihe> kaikkiViestit = (ArrayList) viestiDao.aiheenViestit(aihe.getAihe_id());
                int haluttuSivu = Integer.parseInt(req.params("s"));
                Sivu sivut = new Sivu(kaikkiViestit.size(), haluttuSivu, "location.href='/aiheet/" + aihe.getAihe_id() + "/sivu/", "'");
                map.put("viestit", kaikkiViestit.subList(sivut.getEkaRivi(), sivut.getVikaRivi()+1));          
                map.put("sivut", sivut);
                return new ModelAndView(map, "viestit");
            }
        }, new ThymeleafTemplateEngine());
        
        // Lisätään uusi viesti ja palataan viestilista sivulle
        post("/viesti/:aihe_id", (req, res) -> {
            String viesti = req.queryParams("viesti").trim();
            String nimimerkki = req.queryParams("nimimerkki").trim();
            int aihe_id = Integer.parseInt(req.params("aihe_id"));
            
            if (!viesti.isEmpty()&&!nimimerkki.isEmpty()&&viesti.length()<501&&nimimerkki.length()<26&&aiheDao.findOne(aihe_id)!=null) {
                Viesti uusiViesti = new Viesti(aihe_id, viesti, nimimerkki);
                viestiDao.create(uusiViesti);
            } else {
                res.redirect("/virhe/viesti/"+aihe_id);
                return "";              
            }
            int viimeinenSivu = (viestiDao.aiheenViestit(aihe_id).size()-1)/10+1;          
            res.redirect("/aiheet/"+aihe_id+"/sivu/"+viimeinenSivu);
            return "";
        });
        
        // POST kutsuissa tapahtunut virhe, Ohjataan siis virhe sivulle
        get("/virhe/:viesti/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            String viesti = req.params("viesti");
            if(viesti.equals("alue")) {
                map.put("virhekoodi", "Saman niminen alue on jo olemassa.");
                map.put("uusisivu", "/");
                map.put("sivunnimi", "Pääsivulle");
            }
            if(viesti.equals("aihe")) {
                map.put("virhekoodi", "Uuden keskustelun avauksen luonti epäonnistui.");
                map.put("uusisivu", "/alueet/"+req.params("id"));
                map.put("sivunnimi", "Takaisin aihe alueelle.");
            }     
           if(viesti.equals("viesti")) {
                map.put("virhekoodi", "Uuden viestin luonti epäonnistui.");
                map.put("uusisivu", "/aiheet/"+req.params("id"));
                map.put("sivunnimi", "Takaisin viestiketjuun.");
            }                
            return new ModelAndView(map, "virhe");
        }, new ThymeleafTemplateEngine()); 
        
        while(true) {
            System.out.println("Valitse;");
            System.out.println("1. Näytä kaikki alueet");
            System.out.println("2. Näytä halutun alueen kaikki aiheet");
            System.out.println("3. Näytä halutun aiheen kaikki viestit");
            int valinta = Integer.parseInt(lukija.nextLine());
            switch(valinta) {
                case 1: 
                    for(Alue alue: alueDao.findAll()) {
                        System.out.println(alue);
                    }
                    break;
                case 2: 
                    System.out.print("Anna alue_id: ");
                    int alue = Integer.parseInt(lukija.nextLine());
                    if(alueDao.findOne(alue)!=null) {
                        for(Aihe aihe: aiheDao.alueenAiheet(alue)) {
                            System.out.println(aihe);
                        }
                    } else {
                        System.out.println("EI löydy aluetta: " + alue);
                    }
                    break;
                case 3:
                    System.out.print("Anna aihe_id: ");
                    int aihe = Integer.parseInt(lukija.nextLine());
                    if(aiheDao.findOne(aihe)!=null) {
                        for(Viesti viesti: viestiDao.aiheenViestit(aihe)){
                            System.out.println(viesti);
                        }
                    } else {
                        System.out.println("Ei löydy aihetta: " + aihe);
                    }
                    break;            
            }
        }
        
    }
    
}