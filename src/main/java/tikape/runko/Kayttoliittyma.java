package tikape.runko;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import static spark.Spark.get;
import static spark.Spark.post;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class Kayttoliittyma {

    private Database database;
    private KayttajaDao kayttajaDao;
    private KeskusteluDao keskusteluDao;
    private ViestiDao viestiDao;
    private List<Integer> otsikkoIdt;
    private Kayttaja kayttaja;

    public Kayttoliittyma(Database database) throws SQLException {
        this.database = database;
        this.kayttajaDao = new KayttajaDao(database);
        this.keskusteluDao = new KeskusteluDao(database);
        this.viestiDao = new ViestiDao(database);
    }

    public void suorita() throws SQLException {
        List<Keskustelu> aihealueet = keskusteluDao.getAihealueet();
        List<Keskustelu> otsikot = keskusteluDao.getOtsikot();
        
        
        
        get("/", (req, res) -> {
            String nakyma = nakymanLuoja(aihealueet, "Aihealueet", false);
            return nakyma;
        });
        
        for (Keskustelu aihealue : aihealueet) {
            get("/" + aihealue.getAihealue(), (req, res) -> {
                String nakyma = nakymanLuoja(otsikot, aihealue.getAihealue(), false);
                return nakyma;
            });
        }
<<<<<<< HEAD

=======
        
        for (Keskustelu otsikko : otsikot) {
            get("/" + otsikko.getID(), (req, res) -> {
                String nakyma = nakymanLuoja(otsikot, otsikko.getOtsikko(), true);
                return nakyma;
            });
        }
             
>>>>>>> cf76fc1069b1e7fa59bed6f1cefb470da4b528cf
        get("/kirjautuminen", (req, res) -> {
            String nakyma = kirjautumisSivu();
            return nakyma;
        });
<<<<<<< HEAD

        get("/testi", (req, res) -> {
=======
        
        get("/ketjuviestit", (req, res) -> {
>>>>>>> cf76fc1069b1e7fa59bed6f1cefb470da4b528cf
            String nakyma = "";
            List<Viesti> lista = viestiDao.getKetjuviestit(1);
            for (Viesti viesti : lista) {
                nakyma += viesti.getSisalto() + ".<br>";
            }

            return nakyma;
        });
        
        get("/uusimmat", (req, res) -> {
            String nakyma = "";
            List<Viesti> lista = viestiDao.getUusimmatviestit(2);
            for (Viesti viesti : lista){
                nakyma += viesti.getSisalto() + ".<br>";
            }
            
            return nakyma;
        });       
        
        
        
        get("/logout", (req, res) -> {
            kayttaja = null;
            String nakyma = "<a href=\"/\"><H1>Aneereforum</H1></a>"
                    + "Kirjauduttu ulos";
            return nakyma;
        });

        /* TÃ¤Ã¤ ollu kahteen kertaan */
//        get("/luotunnus", (req, res) -> {
//            String nakyma = luoTunnusSivu();
//            return nakyma;
//        });

        post("/lahetetty", (req, res) -> {
            return "Viesti lÃ¤hetetty!<br><a href=\"/\">Palaa forumille!</a>";
        });

        post("/kirjautuminen", (req, res) -> {
            String tunnus = req.queryParams("tunnus");
            String salasana = req.queryParams("salasana");
            Kayttaja kirjautuva = new Kayttaja(tunnus, salasana);
            if (kayttajaDao.kirjaudu(kirjautuva)) {
                kayttaja = kayttajaDao.getKayttaja(tunnus);
                return "<a href=\"/\"><H1>Aneereforum</H1></a>"
                        + "Kirjautuminen onnistui!";
            } else {
                return "<a href=\"/\"><H1>Aneereforum</H1></a>"
                        + "VÃ¤Ã¤rÃ¤ tunnus tai salasana!";
            }
        });

        post("/luotunnus", (req, res) -> {
            String tunnus = req.queryParams("tunnus");
            String salasana = req.queryParams("salasana");
            String email = req.queryParams("email");
            int id = 0;
            Kayttaja uusi = new Kayttaja(kayttajaDao.setId(), tunnus, salasana, email, 0);

            if (kayttajaDao.getKayttaja(tunnus) == null) {
                kayttajaDao.luoKayttaja(uusi);
                return "<a href=\"/\"><H1>Aneereforum</H1></a>"
                        + "KÃ¤yttÃ¤jÃ¤n luominen onnistui!";
            } else {
                return "<a href=\"/\"><H1>Aneereforum</H1></a>"
                        + "Tunnus on jo kÃ¤ytÃ¶ssÃ¤";
            }
        });
    }


    private String nakymanLuoja(List<Keskustelu> keskustelut) {

        String nakyma = "";
        String alue = "vaiheessa";

        for (Keskustelu keskustelu : keskustelut) {
            nakyma += "<a href=\"" + keskustelu.getId() + "\">" + keskustelu.getOtsikko() + "</a><br/>";
        }


        String palautettava = "";
        
        palautettava += "<a href=\"/\"><H1>Aneereforum</H1></a>";
        if (kayttaja == null) {
            palautettava += "<a href=\"/kirjautuminen\">Kirjaudu</a>";
        } else {
            palautettava += "<a href=\"/omasivu\">" + kayttaja.getTunnus() + "</a> <a href=\"/logout\">Kirjaudu ulos</a>";
        }
        
        if (otsikko.equals("Aihealueet")) { //ALOITUSSIVU
            palautettava += "<H2>" + otsikko + "</H2></a>";
        } else if (onkoKetju == false) {    // MUUT SIVUT
            palautettava += "<a href=\"" + otsikko + "\"><H2>" + otsikko + "</H2></a>";
        } else {
            int keskusteluID = keskusteluDao.getOtsikkoID(otsikko);
            palautettava += "<a href=\"" + keskusteluID + "\"><H2>" + otsikko + "</H2></a>";
        }
        
        String nakyma = "";
        int id = 0;
        if (otsikko.equals("Aihealueet")) {            
            for (Keskustelu keskustelu : keskustelut) {
                nakyma += "<a href=\"" + keskustelu.getAihealue() + "\">" + keskustelu.getAihealue() + "</a><br/>";
            }
        } else if (onkoKetju == false) {;
            for (Keskustelu keskustelu : keskustelut) {
                if(keskustelu.getAihealue().equals(otsikko)) {
                    nakyma += "<a href=\"" + keskustelu.getID() + "\">" + keskustelu.getOtsikko() + "</a><br/>";
                }
            }
        } else {
            Keskustelu keskustelu = keskusteluDao.getOtsikko(otsikko);
            id = keskustelu.getID();
            List<Viesti> viestit = viestiDao.getKetjuviestit(id);
//            nakyma += "<a href=\"" + keskustelu.getId() + "\">" + keskustelu.getOtsikko() + "</a><br/>";
            for (Viesti viesti : viestit) {
                nakyma += viesti.toString() + "<br>";
            }
                    nakyma += "<br><br>"
                    + "<form method=\"POST\" action=\"/lahetetty\">\n"
                    + "LisÃ¤Ã¤ viesti ketjuuni:<br/>\n"
                    + "<input type=\"text\" name=\"" + keskustelu.getID() + "\"/><br/>\n"
                    + "<input type=\"submit\" value=\"LisÃ¤Ã¤ viesti\"/>\n"
                    + "</form>";
        }
        
        palautettava += nakyma;

        return palautettava;
    }

    private String kirjautumisSivu() {
        return "<a href=\"/\"><H1>Aneereforum</H1></a>"
                + "EikÃ¶ tunnusta? Tee tunnus <a href=\"/luotunnus\">tÃ¤stÃ¤</a><br><br>"
                + "<form method=\"POST\" action=\"/kirjautuminen\">\n"
                + "Tunnus:<br/>\n"
                + "<input type=\"text\" name=\"tunnus\"/><br/>\n"
                + "Salasana:<br/>"
                + "<input type=\"text\" name=\"salasana\"/><br/>\n"
                + "<input type=\"submit\" value=\"Kirjaudu\"/>\n"
//<<<<<<< HEAD
                + "<a href=\"/luotunnus\">Luo tunnus</a>"
//                + "<input type=\"submit\" value=\"Luo tunnus\"/>\n"
//=======
//>>>>>>> cf76fc1069b1e7fa59bed6f1cefb470da4b528cf
                + "</form>";

        // luo tunnus -napista siirtyminen oikealle sivulle
    }

    private String luoTunnusSivu() {
        return "<a href=\"/\"><H1>Aneereforum</H1></a>"
                + "<form method=\"POST\" action=\"/luotunnus\">\n"
                + "Email:<br/>\n"
                + "<input type=\"text\" name=\"email\"/><br/>\n"
                + "Tunnus:<br/>\n"
                + "<input type=\"text\" name=\"tunnus\"/><br/>\n"
                + "Salasana:<br/>"
                + "<input type=\"text\" name=\"salasana\"/><br/>\n"
                + "<input type=\"submit\" value=\"Luo\"/>\n"
                + "</form>";

    }
}
