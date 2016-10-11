package tikape.runko;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import spark.ModelAndView;
import static spark.Spark.get;
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
        this.database = new Database("jdbc:sqlite:foorumi2.db");
        database.init();
        this.alueDao = new AlueDao(database);
        this.keskusteluDao = new KeskusteluDao(database, alueDao);
        this.viestiDao = new ViestiDao(database, keskusteluDao);

    }

    public HashMap getIndexpage() throws Exception {
        HashMap map = new HashMap<>();
        ArrayList<Alue> alueet = new ArrayList<>();
        ArrayList<Integer> viestejaYht = new ArrayList<>();
        ArrayList<Timestamp> viimViesti = new ArrayList<>();

        for (Alue a : viestiDao.alueetJarjestys()) {
            alueet.add(a);
            viestejaYht.add(viestiDao.alueenViestienmaara(a.getId()));
            viimViesti.add(viestiDao.alueenUusin(a.getId()));
        }

        map.put("alue", alueet);
        map.put("yht", viestejaYht);
        map.put("viim", viimViesti);

        return map;
    }

    public void getAluepage(int alueId) throws Exception {
        for (Keskustelu k : viestiDao.keskustelutJarjestys(alueId)) {
            System.out.println(k.getOtsikko() + "\t\t" + viestiDao.keskustelunViestienmaara(k.getId())
                    + '\t' + viestiDao.keskustelunUusin(k.getId()));
        }
    }

    public void run() throws Exception {
        get("/", (req, res) -> {
            HashMap map = getIndexpage();
            return new ModelAndView(map, "index");
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
                                + "\n Keskustelun avaaja:" + k.getAloittaja() + " " + k.getDate()
                                + "\n Otsikko:" + k.getOtsikko()
                                + "\n Viesti:" + k.getAloitusviesti());
                    }
                    break;
                case "Viesti":
                    for (Viesti v : viestiDao.findAll()) {
                        System.out.println("\n Keskustelun avaaja: " + v.getOmakeskustelu().getAloittaja() + " " + v.getOmakeskustelu().getDate()
                                + "\n Keskustelun otsikko: " + v.getOmakeskustelu().getOtsikko()
                                + "\n Avausviesti: " + v.getOmakeskustelu().getAloitusviesti()
                                + "\n Vastaajan nimimerkki: " + v.getKirjoittaja() + " " + v.getViestiDate()
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
