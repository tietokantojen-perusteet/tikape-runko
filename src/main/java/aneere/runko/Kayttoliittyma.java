package aneere.runko;

import aneere.runko.database.Database;
import aneere.runko.database.KayttajaDao;
import aneere.runko.database.KeskusteluDao;
import aneere.runko.database.ViestiDao;
import aneere.runko.domain.Kayttaja;
import aneere.runko.domain.Keskustelu;
import aneere.runko.domain.Viesti;
import java.sql.SQLException;
import java.util.List;
import static spark.Spark.get;
import static spark.Spark.post;

public class Kayttoliittyma {

    private Database database;
    private KayttajaDao kayttajaDao;
    private KeskusteluDao keskusteluDao;
    private ViestiDao viestiDao;
    private Kayttaja kayttaja;
    private int tempKeskusteluID;
    private List<Keskustelu> otsikot;

    public Kayttoliittyma(Database database) throws SQLException {
        this.database = database;
        this.keskusteluDao = new KeskusteluDao(database);
        this.viestiDao = new ViestiDao(database);
        this.kayttajaDao = new KayttajaDao(database);
        this.otsikot = keskusteluDao.getOtsikot();
    }

    public void suorita() throws SQLException {
        List<Keskustelu> aihealueet = keskusteluDao.getAihealueet();

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

        for (Keskustelu otsikko : otsikot) {
            get("/" + otsikko.getID(), (req, res) -> {
                String nakyma = nakymanLuoja(otsikot, otsikko.getOtsikko(), true);
                return nakyma;
            });
        }

        get("/kirjautuminen", (req, res) -> {
            String nakyma = kirjautumisSivu();
            return cssLuoja(nakyma);
        });

        get("/logout", (req, res) -> {
            kayttaja = null;
            String nakyma = "<a href=\"/\"><H1>Aneereforum</H1></a>"
                    + "Kirjauduttu ulos";
            return cssLuoja(nakyma);
        });

        get("/luotunnus", (req, res) -> {
            String nakyma = luoTunnusSivu();
            return cssLuoja(nakyma);
        });
        get("/luokeskustelu", (req, res) -> {
            String nakyma = luoKeskusteluSivu();
            return cssLuoja(nakyma);
        });

        get("/omasivu", (req, res) -> {
            String nakyma = omaSivu();
            return cssLuoja(nakyma);
        });

        get("/admin", (req, res) -> {
            String nakyma = adminSivu();
            return cssLuoja(nakyma);
        });
        
        post("/lahetetty", (req, res) -> {
            String sisalto = req.queryParams("sisalto");
            if (kayttaja == null) {
                kayttaja = kayttajaDao.findOne(4);
            }
            Viesti lisattava = new Viesti(viestiDao.getSeuraavaID(), kayttaja.getID(), getKeskusteluID(), sisalto);
            viestiDao.lisaaViesti(lisattava);
            return cssLuoja("Viesti lähetetty!");
        });

        post("/kirjautuminen", (req, res) -> {
            String tunnus = req.queryParams("tunnus");
            String salasana = req.queryParams("salasana");
            Kayttaja kirjautuva = new Kayttaja(tunnus, salasana);
            String nakyma = "";
            if (kayttajaDao.kirjaudu(kirjautuva)) {
                kayttaja = kayttajaDao.getKayttaja(tunnus);
                nakyma = "Kirjautuminen onnistui!";
            } else {
                nakyma = "Väärä tunnus tai salasana!";
            }
            return cssLuoja(nakyma);
        });

        post("/luotunnus", (req, res) -> {
            String tunnus = req.queryParams("tunnus");
            String salasana = req.queryParams("salasana");
            String email = req.queryParams("email");
            Kayttaja uusi = new Kayttaja(kayttajaDao.getSeuraavaID(), tunnus, salasana, email, 0);

            if (kayttajaDao.getKayttaja(tunnus) == null) {
                kayttajaDao.luoKayttaja(uusi);
                return cssLuoja("Käyttäjän luominen onnistui!");
            } else {
                return cssLuoja("Tunnus on jo käytössä");
            }
        });

      post("/luokeskustelu", (req, res) -> {
            String aihealue = req.queryParams("aihealue");
            String otsikko = req.queryParams("otsikko");
            String salasana = req.queryParams("salasana");
            if (kayttaja != null && kayttaja.tarkistaSalasana(salasana)) {
                Keskustelu uusi = new Keskustelu(keskusteluDao.getSeuraavaID(), otsikko, aihealue);
                keskusteluDao.luoKeskustelu(uusi);
                otsikot = keskusteluDao.getOtsikot();
                suorita();
                return cssLuoja("Keskustelu lisätty");
            }
            return cssLuoja("Väärä salasana");
        });
      
        post("/admin", (req, res) -> {
            String viestiID = req.queryParams("viestiID");
            String keskusteluID = req.queryParams("keskusteluID");
            String salasana = req.queryParams("salasana");
            if(!salasana.equals("poista")) {
                return cssLuoja("Väärä salasana");
            } else {
                if (!viestiID.isEmpty()) {
                    int poistettavaViestiID = Integer.parseInt(viestiID);
                    if (viestiDao.poistaViesti(poistettavaViestiID)) {
                        return cssLuoja("Viestin poisto onnistui! palaa takaisin <a href=\"/admin\"> tästä</a>");
                    } 
                } else if(!keskusteluID.isEmpty()) {
                    int poistettavaKeskusteluID = Integer.parseInt(keskusteluID);
                    if (keskusteluDao.poistaKeskustelu(poistettavaKeskusteluID)) {
                        return cssLuoja("Keskustelun poisto onnistui! palaa takaisin <a href=\"/admin\"> tästä</a>");
                    }
                }
                return cssLuoja("Viestin tai keskustelun poisto ei onnistunut, palaa takaisin <a href=\"/admin\"> tästä</a>");
            }
        });
        
    }

    private String nakymanLuoja(List<Keskustelu> keskustelut, String otsikko, boolean onkoKetju) throws SQLException {

        String nakyma = "";
        nakyma += "<H2><a href=\"/\">Aihealueet</a> ";

        if (onkoKetju == false && !otsikko.equals("Aihealueet")) {    // MUUT SIVUT
            nakyma += " ---> <a href=\"" + otsikko + "\">" + otsikko + "</a>" 
            + "  <a href=\"luokeskustelu\">[Lisää uusi otsikko]</a></H2>";
        } else if (onkoKetju == true) {
            int keskusteluID = keskusteluDao.getOtsikkoID(otsikko);
            String aihealue = keskusteluDao.getAihealue(keskusteluID);
            nakyma += "---> <a href=\"" + aihealue + "\">" + aihealue + "</a> --->   ";
            nakyma += "<a href=\"" + keskusteluID + "\">" + otsikko + "</a></H2>";
        } else {
            nakyma += "</H2>";
        }

        int id = 0;
        if (otsikko.equals("Aihealueet")) {
            for (Keskustelu keskustelu : keskustelut) {
                nakyma += "<a href=\"" + keskustelu.getAihealue() + "\">" + keskustelu.getAihealue() + "</a><br/>";
            }
        } else if (onkoKetju == false) {
            for (Keskustelu keskustelu : keskustelut) {
                if (keskustelu.getAihealue().equals(otsikko)) {
                    nakyma += "<a href=\"" + keskustelu.getID() + "\">" + keskustelu.getOtsikko() + "</a><br/>";
                }
            }
        } else {
            Keskustelu keskustelu = keskusteluDao.getOtsikko(otsikko);
            id = keskustelu.getID();
            List<Viesti> viestit = viestiDao.getKetjuviestit(id);
            for (Viesti viesti : viestit) {
                nakyma += viesti.toString() + "<br>";
            }

            nakyma += "<br><br>"
                    + "<form method=\"POST\" action=\"/lahetetty\">\n"
                    + "Lisää viesti ketjuuni:<br/>\n"
                    + "<input type=\"text\" name=\"sisalto\"/><br/>\n"
                    + "<input type=\"submit\" value=\"Lisää viesti\"/>\n"
                    + "</form>";
            setKeskusteluID(keskustelu.getID());
        }

        return cssLuoja(nakyma);
    }

    private String cssLuoja(String nakyma) throws SQLException {
        int keskustelujenMaara = keskusteluDao.getSeuraavaID()-1;
        int viestienMaara = viestiDao.getSeuraavaID()-1;
        int kayttajienMaara = kayttajaDao.getSeuraavaID()-1;
        
        String ylaosa = "<a href=\"/\"><H1>Aneereforum</H1></a>";
        String alaosa = "Forumilla yhteensä " + kayttajienMaara + " käyttäjää, " + keskustelujenMaara +
                        " keskustelua, joissa " + viestienMaara + " viestiä!";
        String kirjautuminen = "";
        if (kayttaja == null) {
            kirjautuminen += "<a href=\"/kirjautuminen\">Kirjaudu</a>";
        } else {
            kirjautuminen += "<a href=\"/omasivu\">" + kayttaja.getTunnus() + "</a> <a href=\"/logout\">Kirjaudu ulos</a>";
        }

        String info = "";
        List<Viesti> uusimmat = viestiDao.getUusimmat(3);
//        for (Viesti viesti : uusimmat) {
//            info += viesti.toString() + "<br>";
//        }

        String cssPalauttaja = "<style type=\"text/css\">\n"
                + ".tg  {border-collapse:collapse;border-spacing:0;}\n"
                + ".tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}\n"
                + ".tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}\n"
                + ".tg .tg-bisz{font-size:20px;font-family:\"Courier New\", Courier, monospace !important;;background-color:#96fffb;vertical-align:top}\n"
                + ".tg .tg-sp5c{font-size:18px;font-family:\"Arial Black\", Gadget, sans-serif !important;;background-color:#96fffb;text-align:center;vertical-align:top}\n"
                + ".tg .tg-o2ji{font-family:\"Arial Black\", Gadget, sans-serif !important;;background-color:#96fffb;vertical-align:top}\n"
                + ".tg .tg-jrdg{font-family:\"Courier New\", Courier, monospace !important;;background-color:#96fffb;vertical-align:top}\n"
                + "a:link {"
                + "color: green;"
                + "}"
                + "a:visited {"
                + "color: green;"
                + "}"
                + "a:hover {"
                + "color: hotpink;"
                + "}"
                + "a:active {"
                + "color: blue;"
                + "}"
                + "</style>\n"
                + "<table class=\"tg\">\n"
                + "  <tr>\n"
                + "    <th class=\"tg-sp5c\">" + ylaosa + "</th>\n"
                + "    <th class=\"tg-o2ji\">" + kirjautuminen + "</th>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td width=\"65%\" class=\"tg-jrdg\">" + nakyma + "</td>\n"
                + "    <td class=\"tg-jrdg\"><u>Uusimmat viestit</u><br><br>" + info + "</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td class=\"tg-bisz\" colspan=\"2\">" + alaosa + "</td>\n"
                + "  </tr>\n"
                + "</table>";

        return cssPalauttaja;
    }

    private String kirjautumisSivu() {
        return "Eikö tunnusta? Tee tunnus <a href=\"/luotunnus\">tästä</a><br><br>"
                + "<form method=\"POST\" action=\"/kirjautuminen\">\n"
                + "Tunnus:<br/>\n"
                + "<input type=\"text\" name=\"tunnus\"/><br/>\n"
                + "Salasana:<br/>"
                + "<input type=\"text\" name=\"salasana\"/><br/>\n"
                + "<input type=\"submit\" value=\"Kirjaudu\"/>\n"
                + "</form>";
        // luo tunnus -napista siirtyminen oikealle sivulle
    }

    private String luoTunnusSivu() {
        return  "<form method=\"POST\" action=\"/luotunnus\">\n"
                + "Tunnus:<br/>\n"
                + "<input type=\"text\" name=\"tunnus\"/><br/>\n"
                + "Salasana:<br/>"
                + "<input type=\"text\" name=\"salasana\"/><br/>\n"
                + "Email:<br/>\n"
                + "<input type=\"text\" name=\"email\"/><br/>\n"
                + "<input type=\"submit\" value=\"Luo\"/>\n"
                + "</form>";

    }
    private String luoKeskusteluSivu() {
        return  "<form method=\"POST\" action=\"/luokeskustelu\">\n"
                + "Aihealue:<br/>\n"
                + "<input type=\"text\" name=\"aihealue\"/><br/>\n"
                + "Otsikko:<br/>"
                + "<input type=\"text\" name=\"otsikko\"/><br/>\n"
                + "Salasana:<br/>\n"
                + "<input type=\"text\" name=\"salasana\"/><br/>\n"
                + "<input type=\"submit\" value=\"Luo\"/>\n"
                + "</form>";
    }
    private String adminSivu() {
                int viestienMaara = viestiDao.getSeuraavaID();
                int keskustelujenMaara = keskusteluDao.getSeuraavaID();
        return  "Viestejä: " + viestienMaara + " ja Keskusteluita " + keskustelujenMaara + "<br></br>"
                + "<form method=\"POST\" action=\"/admin\">\n"
                + "ViestiID:<br/>\n"
                + "<input type=\"text\" name=\"viestiID\"/><br/>\n"
                + "KeskusteluID:<br/>"
                + "<input type=\"text\" name=\"keskusteluID\"/><br/>\n"
                + "Salasana:<br/>\n"
                + "<input type=\"text\" name=\"salasana\"/><br/>\n"
                + "<input type=\"submit\" value=\"Poista\"/>\n"
                + "</form>";
    }

    private String omaSivu() throws SQLException {
        return "Omasivu"
                + "<form method=\"POST\" action=\"/omsivu\">\n"
                + kayttajaDao.haeViestit(kayttaja);
    }

    public void setKeskusteluID(int id) {
        this.tempKeskusteluID = id;
    }

    public int getKeskusteluID() {
        return this.tempKeskusteluID;
    }
}
