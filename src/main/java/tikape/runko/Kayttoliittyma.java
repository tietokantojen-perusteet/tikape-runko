package tikape.runko;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class Kayttoliittyma {

    private static Scanner lukija = new Scanner(System.in);
    private static Database database;
    private static AlueDao alueDao;
    private static KeskusteluDao keskusteluDao;
    private static ViestiDao viestiDao;

    public Kayttoliittyma() throws Exception {
        this.database = new Database("jdbc:sqlite:foorumi.db");
        database.init();
        this.alueDao = new AlueDao(database);
        this.keskusteluDao = new KeskusteluDao(database, alueDao);
        this.viestiDao = new ViestiDao(database, keskusteluDao);

    }

    public HashMap getIndexpage() throws Exception {
        HashMap map = new HashMap<>();
        ArrayList<Alue> alueet = viestiDao.alueetJarjestys();
        ArrayList<Tempolio> alueTiedot = new ArrayList<>();

        for (Alue a : alueet) {
            String viestMaara = Integer.toString(viestiDao.alueenViestienmaara(a.getId()));
            String aika = "-";
            
            if (viestiDao.alueenUusin(a.getId()) != null) {
                String str = viestiDao.alueenUusin(a.getId()).toString();
                aika = str.substring(0, 19);
            }
            alueTiedot.add(new Tempolio(a, viestMaara, aika));
        }

        map.put("alue", alueet);
        map.put("aluetiedot", alueTiedot);

        return map;
    }

    public HashMap getAluepage(int alueId) throws Exception {
        HashMap map = new HashMap<>();
        
        ArrayList<Keskustelu> keskustelut = viestiDao.keskustelutJarjestys(alueId);
        ArrayList<Keskustelu> valmiit = new ArrayList<>();
        int montako = keskustelut.size();
        if(montako > 10){
            for(int i = 0; i<10; i++){
                valmiit.add(keskustelut.get(i));
            }
        }else{
            valmiit = keskustelut;
        }
        ArrayList<Tempolio> keskTiedot = new ArrayList<>();
        String aluenimi = alueDao.findOne(alueId).getNimi();
        
        for (Keskustelu k : valmiit) {
            String viestMaara = Integer.toString(viestiDao.keskustelunViestienmaara(k.getId()));
            String aika;
            
            if (viestiDao.keskustelunUusin(k.getId()) != null) {
                String str = viestiDao.keskustelunUusin(k.getId()).toString();
                aika = str.substring(0, 19);
            } else {
                String str = keskusteluDao.findOne(k.getId()).getTime().toString();
                aika = str.substring(0, 19);
            }
            
            keskTiedot.add(new Tempolio(k, viestMaara, aika));
        }
        
        map.put("alueennimi", aluenimi);
        map.put("kesktiedot", keskTiedot);
        map.put("alueid", alueId);

        return map;
    }


    public HashMap getKeskustelupage(int keskusteluId, int sivunumero) throws Exception {
        HashMap map = new HashMap<>();
        ArrayList<Viesti> viesti = viestiDao.viestitJarjestys(keskusteluId);
        ArrayList<Viesti> valmis = new ArrayList<>();
        int pituussivulla = viesti.size()- 10*(sivunumero-1);
        if(pituussivulla >= 9 && sivunumero ==1){
            for(int i =0; i <9; i++ ){
           
                valmis.add(viesti.get(i));
            }
        }else if(pituussivulla <= 10){
            for(int i =0; i <pituussivulla; i++ ){
           
                valmis.add(viesti.get((sivunumero-1)*10 + i));
            }
        }else{
           for(int i =0; i <10; i++ ){
           
                valmis.add(viesti.get((sivunumero-1)*10 + i));
            } 
        }    
        Keskustelu kesk = keskusteluDao.findOne(keskusteluId);

        String aika = kesk.getTime().toString();
        aika = aika.substring(0,19);
        
        
        map.put("viesti", valmis);
       
        map.put("omakeskus", kesk);
        if(sivunumero ==1){
            map.put("aloittaja", kesk.getAloittaja());
            map.put("aloitusviesti", kesk.getAloitusviesti());
            map.put("julkaisuaika", aika);
        }else{
            map.put("aloittaja", "");
            map.put("aloitusviesti", "");
            map.put("julkaisuaika", "");
            
        }
        map.put("omasivu", sivunumero);
        
        if(sivunumero > 1){
             map.put("numeroedel", sivunumero -1);
        }else{
             map.put("numeroedel", 1);
        }
        if(pituussivulla <= 10){
           map.put("numeroseur", sivunumero);
        }else{
           map.put("numeroseur", sivunumero+1); 
        }


        return map;
    }
    public void run() throws Exception {
        ArrayList<String> alueet = new ArrayList<>();

        staticFileLocation("/templates");

        get("/", (req, res) -> {
            HashMap map = getIndexpage();
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/alue/:id", (req, res) -> {
            HashMap map = getAluepage(Integer.parseInt(req.params("id")));
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());
        
        get("/alue/keskustelu/:id/:numero", (req, res) -> {
            HashMap map = getKeskustelupage(Integer.parseInt(req.params(":id")), Integer.parseInt(req.params(":numero")));
            return new ModelAndView(map, "keskustelu");
            
        }, new ThymeleafTemplateEngine());
        
        post("/", (req, res) -> {
            String nimi = req.queryParams("nimi");
            alueDao.lisaaAlue(nimi);
            HashMap map = getIndexpage();
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/alue/:id", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String otsikko = req.queryParams("otsikko");
            String viesti = req.queryParams("viesti");
            keskusteluDao.lisaaKeskustelu(alueDao.findOne(Integer.parseInt(req.params(":id"))).getId(), otsikko, nimi, viesti);
            HashMap map = getAluepage(Integer.parseInt(req.params(":id")));
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());
        
        post("/alue/keskustelu/:id/:numero", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String viesti = req.queryParams("viesti");
            viestiDao.lisaaViesti(keskusteluDao.findOne(Integer.parseInt(req.params(":id"))).getId(), nimi, viesti);
            HashMap map = getKeskustelupage(Integer.parseInt(req.params(":id")), Integer.parseInt(req.params(":numero")));
            return new ModelAndView(map, "keskustelu");
        }, new ThymeleafTemplateEngine());

    }

    public void getAllTables() throws Exception {

        while (true) {

            System.out.println("Listaa kaikki tauluista kirjoittamalla taulun nimi: "
                    + '\n' + "Alue" + '\n' + "Keskustelu" + '\n' + "Viesti" + "\n\n" + "Tyhjä lopettaa.");
            System.out.print("> ");
            String vastaus = lukija.nextLine();

            if (vastaus.equals("")) {
                break;
            }

            switch (vastaus) {
                case "Alue":
                    for (Alue a : alueDao.findAll()) {
                        System.out.println("\n" + a.getNimi());
                    }
                    break;
                case "Keskustelu":
                    for (Keskustelu k : keskusteluDao.findAll()) {
                        System.out.println("\n Alue: " + k.getOmaalue().getNimi()
                                + "\n Keskustelun avaaja:" + k.getAloittaja() + " " + k.getTime()
                                + "\n Otsikko:" + k.getOtsikko()
                                + "\n Viesti:" + k.getAloitusviesti());
                    }
                    break;
                case "Viesti":
                    for (Viesti v : viestiDao.findAll()) {
                        System.out.println("\n Keskustelun avaaja: " + v.getOmakeskustelu().getAloittaja() + " " + v.getOmakeskustelu().getTime()
                                + "\n Keskustelun otsikko: " + v.getOmakeskustelu().getOtsikko()
                                + "\n Avausviesti: " + v.getOmakeskustelu().getAloitusviesti()
                                + "\n Vastaajan nimimerkki: " + v.getKirjoittaja() + " " + v.getViestiTime()
                                + "\n Vastaajan viesti: " + v.getTeksti());
                    }
                    break;
                default:
                    System.out.println("Taulua ei löydetty. \n");
                    ;
                    break;
            }
        }
    }
}
