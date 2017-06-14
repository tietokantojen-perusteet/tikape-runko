package tikape.runko;

import java.sql.SQLException;
import java.util.Scanner;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class TekstiUI {

    public static void luoKayttoliittyma(AlueDao alueDao, AiheDao aiheDao, ViestiDao viestiDao) throws SQLException {
        Scanner lukija = new Scanner(System.in);

        while (true) {
            int valinta;

            System.out.print("\nValitse:\n"
                           + "1. Näytä kaikki alueet\n"
                           + "2. Näytä halutun alueen kaikki aiheet\n"
                           + "3. Näytä halutun aiheen kaikki viestit\n"
                           + "\n> ");
            try {
                valinta = Integer.parseInt(lukija.nextLine());
            } catch (NumberFormatException e) {
                continue;
            }

            switch (valinta) {
                case 1:
                    for (Alue alue : alueDao.findAll()) {
                        System.out.println(alue);
                    }

                    break;
                case 2:
                    int alue;

                    System.out.print("Anna alue_id: ");
                    try {
                        alue = Integer.parseInt(lukija.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Virheellinen syöte!");
                        continue;
                    }

                    if (alueDao.findOne(alue) != null) {
                        for (Aihe aihe : aiheDao.findAllIn(alue)) {
                            System.out.println(aihe);
                        }
                    } else {
                        System.out.println("Ei löydy aluetta: " + alue);
                    }

                    break;
                case 3:
                    int aihe;

                    System.out.print("Anna aihe_id: ");
                    try {
                        aihe = Integer.parseInt(lukija.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Virheellinen syöte!");
                        continue;
                    }

                    if (aiheDao.findOne(aihe) != null) {
                        for (Viesti viesti : viestiDao.findAllIn(aihe)) {
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