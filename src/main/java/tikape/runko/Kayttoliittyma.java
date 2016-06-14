package tikape.runko;

import java.sql.SQLException;
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
        List<String> aihealueet = keskusteluDao.getAihealueet();    
        get("/", (req, res) -> {                    
            String nakyma = nakymanLuoja(aihealueet, "Aihealueet");         
            return nakyma;            
        });       

        for (String aihealue : aihealueet) {
            get("/" + aihealue, (req,res) -> {
                List<String> otsikot = keskusteluDao.getOtsikot(aihealue);
                String nakyma = nakymanLuoja(otsikot, aihealue);            
                return nakyma;
            });
        }       
        
        get("/kirjautuminen", (req,res) -> {
                String nakyma = kirjautumisSivu();
                return nakyma;
        });
        
        get("/logout", (req,res) -> {
                kayttaja = null;
                String nakyma = "<a href=\"/\"><H1>Aneereforum</H1></a>"
                        + "Kirjauduttu ulos";
                return nakyma;
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
                    + "Väärä tunnus tai salasana!";
            }
        });
    }
    private String nakymanLuoja(List<String> lista, String alue) {
        
        String nakyma = "";

        for (String otsikko : lista) {
            nakyma += "<a href=\"" + otsikko + "\">" + otsikko + "</a><br/>";
        }
        
        String palautettava = "";
        palautettava += "<a href=\"/\"><H1>Aneereforum</H1></a>";
        if (kayttaja == null) {
            palautettava += "<a href=\"/kirjautuminen\">Kirjaudu</a>";
        } else {
            palautettava += "<a href=\"/omasivu\">" + kayttaja.getTunnus() + "</a> <a href=\"/logout\">Kirjaudu ulos</a>";
        }
        if (alue.equals("Aihealueet")) { //ALOITUSSIVU
                palautettava += "<H2>" + alue + "</H2></a>";           
        } else {    // MUUT SIVUT
                palautettava += "<a href=\"" + alue + "\"><H2>" + alue + "</H2></a>";
        }
        palautettava += nakyma;
        
        return palautettava;
    }
    private String kirjautumisSivu() {
        return  "<a href=\"/\"><H1>Aneereforum</H1></a>"
                + "<form method=\"POST\" action=\"/kirjautuminen\">\n"
                + "Tunnus:<br/>\n"
                + "<input type=\"text\" name=\"tunnus\"/><br/>\n"
                + "Salasana:<br/>"
                + "<input type=\"text\" name=\"salasana\"/><br/>\n"
                + "<input type=\"submit\" value=\"Kirjaudu\"/>\n"
                + "</form>";
    }
}
