/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko;

import java.sql.SQLException;
import java.util.Scanner;
import tikape.runko.database.*;
import java.util.List;
import tikape.runko.domain.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Samuel
 */
public class Kayttoliityma {

    private Scanner lukija;

    public Kayttoliityma() {
        this.lukija = new Scanner(System.in);
    }


    public void kaynnista() throws ClassNotFoundException, SQLException {
        Database database = new Database("jdbc:sqlite:keskustelufoorumi.db");
        database.init();            // Tämä tosin voidaan suorittaa vain kerran, sillä se sisältää create table-käskyt

        // Dao-olioilmentymät
        KeskustelualueDao keskustelualueDao = new KeskustelualueDao(database);
        KeskustelunavausDao keskustelunavausDao = new KeskustelunavausDao(database);
        ViestiDao viestiDao = new ViestiDao(database);
        System.out.println("Tervetuloa!");

        boolean poistu = false;

        while (poistu == false) {

            while (true) {

                String kasky1 = "";
                while (true) {
                    System.out.println("");
                    System.out.println("Haluatko tarkastella");
                    System.out.println("(1) keskustelualueita,");
                    System.out.println("(2) keskustelunavauksia,");
                    System.out.println("(3) viestejä?");
                    System.out.println(" vai (4) testata SQL-komentoja? (valitse numero tai poistu painamalla x)");
                    System.out.println("");
                    System.out.print("> ");

                    // Tallentaa käskyn muuttajaan
                    kasky1 = this.lukija.nextLine().toLowerCase().trim();
                    if (kasky1.equals("x")) {
                        poistu = true;
                        break;
                    }

                    // SQL-komentojen testaamiseen
                    if (kasky1.equals("4")) {
                        System.out.println("Anna SQL-komento:");
                        String komento = this.lukija.nextLine();
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:testi.db");

                        Statement statement = connection.createStatement();

                        ResultSet resultSet = statement.executeQuery("SELECT 1");

                        if (resultSet.next()) {
                            System.out.println(resultSet);
                        } else {
                            System.out.println("Yhteyden muodostaminen epäonnistui.");
                        }
                        connection.close();
                        
                        break;
                    }

                    // Suorittaa luupin uudestaan, jos syöte ei ole validi
                    if (kasky1.equals("1") || kasky1.equals("2") || kasky1.equals("3")) {
                        break;
                    } else {
                        System.out.println("Tuntematon komento, yritä uudelleen.");
                    }
                }

                // Lopettaa luupin heti alkuun, jos käyttäjä haluaa poistua tai kokeili jo SQL-komentoja
                if (kasky1.equals("4") || kasky1.equals("x")) {
                    break;
                }

                System.out.println("");
                System.out.println("Haluatko (1) etsiä tietyn,");
                System.out.println("(2) listan kaikista");
                System.out.println("vai (3) poistaa tietyn? (valitse numero tai poistu painamalla x)");
                System.out.println("");
                System.out.print("> ");

                String kasky2 = this.lukija.nextLine().toLowerCase().trim();
                if (kasky2.equals("x")) {
                    poistu = true;
                    break;
                }

                // Tallentaa avaimen, jotta se voidaan tarvittaessa välittää Dao-luokkien metodien parametriksi myöhemmin
                int avain = 0;
                if (kasky2.equals("1") || kasky2.equals("3")) {
                    System.out.println("");
                    System.out.println("Anna avain (numero)");
                    System.out.print("> ");
                    avain = Integer.parseInt(this.lukija.nextLine());
                }

                // Käydään läpi vaihtoehto kerrallaan ja käytetään daojen metodeja
                if (kasky1.equals("1")) {
                    if (kasky2.equals("1")) {
                        System.out.println(keskustelualueDao.findOne(avain));
                    } else if (kasky2.equals("2")) {
                        List<Keskustelualue> alueet = keskustelualueDao.findAll();
                        for (Keskustelualue current : alueet) {
                            System.out.println(current);
                        }
                    } else if (kasky2.equals("3")) {
                        keskustelualueDao.delete(avain);
                        System.out.println("Ok!");
                    }
                } else if (kasky1.equals("2")) {
                    if (kasky2.equals("1")) {
                        System.out.println(keskustelunavausDao.findOne(avain));
                    } else if (kasky2.equals("2")) {
                        List<Keskustelunavaus> avaukset = keskustelunavausDao.findAll();
                        for (Keskustelunavaus current : avaukset) {
                            System.out.println(current);
                        }
                    } else if (kasky2.equals("3")) {
                        keskustelunavausDao.delete(avain);
                        System.out.println("Ok!");
                    }
                } else if (kasky1.equals("3")) {
                    if (kasky2.equals("1")) {
                        System.out.println(viestiDao.findOne(avain));
                    } else if (kasky2.equals("2")) {
                        List<Viesti> viestit = viestiDao.findAll();
                        for (Viesti current : viestit) {
                            System.out.println(current);
                        }
                    } else if (kasky2.equals("3")) {
                        viestiDao.delete(avain);
                        System.out.println("Ok!");
                    }
                }

                // Ohjaa tarvittaessa luupin alkuun, jos syöte oli epäkelpo
                if (!(kasky2.equals("1") || kasky2.equals("2") || kasky2.equals("3"))) {
                    System.out.println("Tuntematon komento, yritä uudelleen.");
                } else {
                    break;
                }
            }

        }

    }

}
