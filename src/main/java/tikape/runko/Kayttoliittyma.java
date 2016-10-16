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
        List<Alue> alueet = viestiDao.alueetJarjestys();
        ArrayList<String> viestejaYht = new ArrayList<>();
        ArrayList<String> viimViesti = new ArrayList<>();

        for (Alue a : alueet) {
            viestejaYht.add(Integer.toString(viestiDao.alueenViestienmaara(a.getId())));
            if (viestiDao.alueenUusin(a.getId()) != null) {
                String str = viestiDao.alueenUusin(a.getId()).toString();
                viimViesti.add(str.substring(0, 19));
            } else {
                viimViesti.add("-");
            }
        }

        map.put("alue", alueet);
        map.put("yht", viestejaYht);
        map.put("viim", viimViesti);

        return map;
    }

    public HashMap getAluepage(int alueId) throws Exception {
        HashMap map = new HashMap<>();
        List<Keskustelu> keskustelut = viestiDao.keskustelutJarjestys(alueId);
        ArrayList<String> viestejaYht = new ArrayList<>();
        ArrayList<String> viimViesti = new ArrayList<>();
        String aluenimi = alueDao.findOne(alueId).getNimi();
        
        for (Keskustelu k : keskustelut) {
            viestejaYht.add(Integer.toString(viestiDao.keskustelunViestienmaara(k.getId())));

            if (viestiDao.keskustelunUusin(k.getId()) != null) {
                String str = viestiDao.keskustelunUusin(k.getId()).toString();
                viimViesti.add(str.substring(0, 19));
            } else {
                String str = keskusteluDao.findOne(k.getId()).getTime().toString();
                viimViesti.add(str.substring(0, 19));
            }
        }
        map.put("alueennimi", aluenimi);
        map.put("keskustelu", keskustelut);
        map.put("yht", viestejaYht);
        map.put("viim", viimViesti);
        map.put("alueid", alueId);

        return map;
    }


    public HashMap getKeskustelupage(int keskusteluId) throws Exception {
        HashMap map = new HashMap<>();
        List<Viesti> viesti = viestiDao.viestitJarjestys(keskusteluId);
        Keskustelu kesk = keskusteluDao.findOne(keskusteluId);
        String keskusnimi = kesk.getOtsikko();
        String aika = kesk.getTime().toString();
        String aloitti = kesk.getAloittaja();
        String tekstii = kesk.getAloitusviesti();
        
        map.put("viesti", viesti);
        map.put("omakeskus", kesk);
        map.put("julkaisuaika", aika);


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
        
        get("/alue/keskustelu/:id", (req, res) -> {
            HashMap map = getKeskustelupage(Integer.parseInt(req.params("id")));
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
            keskusteluDao.lisaaKeskustelu(alueDao.findOne(Integer.parseInt(req.params("id"))).getId(), otsikko, viesti, viesti);
            HashMap map = getAluepage(Integer.parseInt(req.params("id")));
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());
        
        post("/alue/keskustelu/:id", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String viesti = req.queryParams("viesti");
            viestiDao.lisaaViesti(keskusteluDao.findOne(Integer.parseInt(req.params("id"))).getId(), nimi, viesti);
            HashMap map = getKeskustelupage(Integer.parseInt(req.params("id")));
            return new ModelAndView(map, "alue");
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
