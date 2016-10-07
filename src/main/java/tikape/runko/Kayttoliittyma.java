package tikape.runko;

import java.sql.*;
import java.util.Scanner;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class Kayttoliittyma {

    private static Scanner lukija = new Scanner(System.in);

    public void run() throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi2.db");
        database.init();
        AlueDao alueDao = new AlueDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database, alueDao);
        ViestiDao viestiDao = new ViestiDao(database, keskusteluDao);

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
