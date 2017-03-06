package tikape.runko;

import java.sql.*;
import java.util.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AihealueDao;
import tikape.runko.database.Database;
import tikape.runko.database.KetjuDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.domain.Aihealue;
import tikape.runko.domain.Ketju;
import tikape.runko.domain.Viesti;

public class Main {

    static Integer indeksi;

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi2.db");
        database.init();
        AihealueDao aDao = new AihealueDao(database);
        KetjuDao kDao = new KetjuDao(database);
        ViestiDao vDao = new ViestiDao(database);

        indeksi = kDao.findAll().size();

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            ArrayList<Aihealue> viema = new ArrayList<>();
            for (Aihealue a : aDao.findAll()) {
                int i = 0;
                for (Ketju k : kDao.findByAihealue(a.getAihe())) {
                    i += kDao.viestienMaara(k.getKetju());
                }
                a.setKoko(i);

                ArrayList<Timestamp> al = new ArrayList<>();

                for (Ketju k : kDao.findByAihealue(a.getAihe())) {
                    al.add(Timestamp.valueOf(kDao.getViimeisin(k.getKetju())));
                }

                Collections.sort(al);

                if (!al.isEmpty()) {
                    Timestamp ts = new Timestamp(al.get(al.size() - 1).getTime());
//                    a.setViimeisin(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(ts));
                    a.setViimeisin(ts.toString().split("\\.")[0]);
                }
                viema.add(a);
            }

            map.put("aihealueet", viema);
//            map.put("maarat", viema);
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            String aihe = req.queryParams("aihe");
            System.out.println(aihe);
            if (aihe.isEmpty()) {
                res.redirect("/");
                return null;
            }

            aDao.add(new Aihealue(aihe));
            res.redirect("/");

            return null;
        });

        get("/:aihealue", (req, res) -> {
            HashMap map = new HashMap<>();

            ArrayList<Ketju> ketjut = kDao.findByAihealue(req.params(":aihealue"));
            ArrayList<Ketju> pal = new ArrayList<>();

            if (ketjut.size() < 10) {
                for (Ketju ketju : ketjut) {;
                    ketju.setKoko(kDao.viestienMaara(ketju.getKetju()));
                    ketju.setViimeisin(kDao.getViimeisin(ketju.getKetju()));
                    pal.add(ketju);
                }
            } else {
                
                for (int i = 0; i < 10; i++) {
                    Ketju ketju = ketjut.get(i);
                    ketju.setKoko(kDao.viestienMaara(ketju.getKetju()));
                    ketju.setViimeisin(kDao.getViimeisin(ketju.getKetju()));
                    pal.add(ketjut.get(i));
                }
            }

            map.put("ketjut", pal);
            map.put("aihe", req.params(":aihealue"));
            return new ModelAndView(map, "ketjut");
        }, new ThymeleafTemplateEngine());

        post("/:aihealue", (req, res) -> {
            String otsikko = req.queryParams("Otsikko");
            String sisalto = req.queryParams("Sisalto");
            String kayttajanimi = req.queryParams("Kayttajanimi");

            if (otsikko.isEmpty() || sisalto.isEmpty() || kayttajanimi.isEmpty()) {
                res.redirect("/" + req.params(":aihealue"));
                return null;
            }

            indeksi++;
            Ketju t = new Ketju(indeksi, req.params(":aihealue"), otsikko, sisalto, kayttajanimi);
            kDao.add(t);
            t.setAloitusaika(kDao.getAloitusaika(indeksi));

            res.redirect("/" + req.params(":aihealue"));

            return null;
        });

        get("/:aihealue/:ketjuid/:sivu", (req, res) -> {
            HashMap map = new HashMap<>();
            Ketju k = kDao.findOne(Integer.parseInt(req.params(":ketjuid")));
            Viesti a = new Viesti(k.getKetju(), k.getSisalto(), k.getKayttajanimi());
            ArrayList<Viesti> ka = new ArrayList<>();
            ka.add(a);

            for (Viesti viesti : vDao.findByKetju(k)) {
                ka.add(viesti);
            }
            
            
            
            ArrayList<Viesti> n = new ArrayList<>();
            
            for (int i = Math.max((Integer.parseInt(req.params(":sivu")) - 1) * 10, 0); i < Math.min((Integer.parseInt(req.params(":sivu"))) * 10, ka.size()); i++) {
                ka.get(i).setKommentti((i + 1) + ".   " + ka.get(i).getKommentti());
                n.add(ka.get(i));
            }
            
            map.put("ketju", k);
            map.put("viestit", n);
            map.put("sivumaara", (int)Math.ceil(ka.size() / 10.0));
            System.out.println((int)Math.ceil(ka.size() / 10.0));
            map.put("sivu", Integer.parseInt(req.params(":sivu")));
            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());

        post("/:aihealue/:ketjuid/:sivu", (req, res) -> {
            String sisalto = req.queryParams("Sisalto");
            String kayttajanimi = req.queryParams("Kayttajanimi");

            if (sisalto.isEmpty() || kayttajanimi.isEmpty()) {
                res.redirect("/" + req.params(":aihealue") + "/" + req.params("ketjuid"));
                return null;
            }

            Viesti v = new Viesti(Integer.parseInt(req.params(":ketjuid")), sisalto, kayttajanimi);
            vDao.add(v);

            res.redirect("/" + req.params(":aihealue") + "/" + req.params(":ketjuid") + "/" + req.params(":sivu"));

            return null;

        }, new ThymeleafTemplateEngine());

    }

}
