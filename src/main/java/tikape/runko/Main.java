package tikape.runko;

import tikape.runko.tmp.*;
import tikape.runko.database.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;


public class Main {

	// reseptin luonnin väliaikais tallennus
	static final Resepti_TMP reseptiTMP = new Resepti_TMP();

	public static void main(String[] args) throws Exception {
		File tiedosto = new File("reseptit.db");
		Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());

		AnnosDao annosDao = new AnnosDao(database);
		AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database);

		Spark.get("/reseptit", (req, res) -> {
			List<Annos> annokset = new ArrayList<>();
			List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();

			annokset = annosDao.findAll();

			//Selvitetään seuraavaksi annoksiin sisältyvät raaka-aineet
			//Tallennetaan nämä HashMappiin, jossa avaimena on annos ja avaimeen liittyvänä
			//arvona lista, jossa on annokset raaka-aineet
			HashMap<Annos, List<RaakaAine>> raakaAineet = annosRaakaAineDao.etsiRaakaAineet();

			HashMap map = new HashMap<>();
			map.put("annokset", annokset);
			map.put("raakaAineet", raakaAineet);

			System.out.println(annokset);
			System.out.println(raakaAineet);

			return new ModelAndView(map, "reseptit");
		}, new ThymeleafTemplateEngine());

		Spark.get("/etusivu", (req, res) -> {
			HashMap map = new HashMap<>();

			return new ModelAndView(map, "etusivu");
		}, new ThymeleafTemplateEngine());
		
		
		
		
		// ---------- Alla reseptin luontiin liittyvät jutut -----------------

		Spark.post("/luo_resepti/lisaa_nimi", (req, res) -> {
			reseptiTMP.setNimi(req.queryParams("reseptinNimi"));
			res.redirect("/luo_resepti");
			return "";
		});

		Spark.post("/luo_resepti/lisaa_raakaAine", (req, res) -> {
			reseptiTMP.lisaaRaakaAine(new RaakaAine_TMP(req.queryParams("rkaine"), req.queryParams("maara")));
			res.redirect("/luo_resepti");
			return "";
		});

		Spark.post("luo_resepti/lisaa_ohje", (req, res) -> {
			reseptiTMP.setOhje(req.queryParams("ohjeTeksti"));
			res.redirect("/luo_resepti");
			return "";
		});

		Spark.post("luo_resepti/tallenna_ja_poistu", (req, res) -> {
			// tallennetaan tällä hetkellä vain annos-tauluun
			annosDao.saveOrUpdate(new Annos(-1,reseptiTMP.getNimi(),reseptiTMP.getOhje()));
			reseptiTMP.tyhjenna(); 	
			res.redirect("/etusivu");
			return "";
		});

		Spark.post("luo_resepti/tyhjenna", (req, res) -> {
			reseptiTMP.tyhjenna(); // tyhjennetään resepti
			res.redirect("/luo_resepti"); // palataan reseptin luontiin
			return "";
		});

		Spark.get("/luo_resepti", (req, res) -> {
			HashMap map = new HashMap<>();

			// debuggausta varten
			System.out.println("\nReseptiolion tila tällä hetkellä: \n" + reseptiTMP + "\n");

			map.put("reseptinNimi", reseptiTMP.getNimi());
			map.put("raakaAineet", reseptiTMP.getRaakaAineet());
			map.put("ohje", reseptiTMP.getOhje());

			return new ModelAndView(map, "luo_resepti");
		}, new ThymeleafTemplateEngine());
		
		// --------- yllä reseptin luontiin liittyvät jutut ------------------
		
		
		
	}
}
