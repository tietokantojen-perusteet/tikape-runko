package tikape.runko;

import java.sql.SQLException;
import java.util.List;
import static spark.Spark.get;
import static spark.Spark.post;
import tikape.runko.database.*;

public class Kayttoliittyma {
    private Database database;
    private KayttajaDao kayttajaDao;
    private KeskusteluDao keskusteluDao;
    private ViestiDao viestiDao;
    public Kayttoliittyma(Database database) {
        this.database = database;
        this.kayttajaDao = new KayttajaDao(database);
        this.keskusteluDao = new KeskusteluDao(database);
        this.viestiDao = new ViestiDao(database);
    }
    public void aloitusNakyma() {
        List<String> otsikot = keskusteluDao.getAihealueet();  
        String nakyma = linkkiListaaja(otsikot);
    }
    public void suorita() throws SQLException {
        get("/", (req, res) -> {
            List<String> otsikot = keskusteluDao.getAihealueet();            
            String nakyma = linkkiListaaja(otsikot);         
            return nakyma;            
        });
        
        get("/Kitarat", (req, res) ->  {
            List<String> otsikot = keskusteluDao.getOtsikot("Kitarat");
            String nakyma = linkkiListaaja(otsikot);            
            return nakyma;
        });
        
        get("/Moottoripyörät", (req, res) ->  {
            List<String> otsikot = keskusteluDao.getOtsikot("Moottoripyörät");
            String nakyma = linkkiListaaja(otsikot);            
            return nakyma;
        });

        get("/Puhelimet", (req, res) ->  {
            List<String> otsikot = keskusteluDao.getOtsikot("Puhelimet");            
            String nakyma = linkkiListaaja(otsikot);            
            return nakyma;
        });
        
        post("/kitarat", (req, res) -> {
//            String viesti = req.queryParams("viestit");
//            sisallot.add(viesti);
//            database.lisaaViesti(viesti);
            return "Viesti lähetetty!<br><a href=\"/viestit\">Palaa Vieraskirjaan</a>";
        });
    }
    private String linkkiListaaja(List<String> lista) {
        String nakyma = "";
        for (String otsikko : lista) {
            nakyma += "<a href=\"" + otsikko + "\">" + otsikko + "</a><br/>";
        }

        return "<H1>Aneereforum</H1>"
                + nakyma;
    }
}
