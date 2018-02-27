# Punaisen Lentävän Virtahevon reseptiarkisto

## Tehtävän tavoite
Projektin tavoitteena on tehdä verkkopohjainen reseptiarkistosovellus.
Ohjelman toiminta perustuu tietokantaan, johon tallennetaan käyttäjän sinne valitsemia Ruoka-, tai juomaohjeita. Sen käyttöliittymä on yksinkertainen selaimen tulkitseva verkkosivu, jonka esittäminen toteutetaan java-pohjaisena ohjelmana. Ohjelma tallentaa käyttäjän antamat reseptit java-toteutuksen avulla itse tietokantaan, ja noutaa käyttäjän pyytämät tiedot tietokannasta myöskin java-rajapinnan kautta.

___

## Ohjelma koostuu seuraavista osista:
1. SQL-pohjainen relaatiotietokanta, johon tieto tallenetaan.
2. Javaohjelma, joka sisältää tietokantaa vastaavat olioluokat, tietokanta-abstraktion ja DatabaseAccessObject-luokat.
3. HTML-verkkosivut, joiden ulkoasu tulkitaan käyttäjän valitsemalla selaimella.

___

## Ohjelmakuvaus

### Pääsivu
Avatessaan ohjelman pääsivun, käyttäjä näkee arkiston nimen, ja listauksen ohjelmaan tallennetuista resepteistä. Käyttäjä voi valita minkä tahansa listatuista resepteistä painamalla sen nimeä hiirellä, mikä avaa reseptiä vastaavan reseptisivun selaimessa. Vaihtoehtoisesti käyttäjä voi avata uuden reseptin hallinnointisivun, tai reseptien raaka-aineiden tarkastelu- ja hallinnointisivun. Raaka-aineiden tarkastelusivulla on jokaisen raaka-aineen kohdalla linkki sivulle, jossa näytetään kuinka monessa reseptissä kyseinen raaka-aine esiintyy.
___

### Reseptisivu
Avatessaan reseptisivun, käyttäjä näkee reseptin nimen, sekä listauksen reseptin raaka-aineista, niiden kappalemääristä ja lisäysjärjestyksistä. Reseptiin voi myös liittyä erillinen ohje, joka myös näytetään käyttäjälle. Halutessaan, käyttäjä pääsee takaisin ohjelman pääsivulle painamalla sivulla olevaa linkkiä.
____

### Reseptien Hallinnoitisivu
Uuden reseptin hallinnointisivulla käyttäjä näkee listauksen resepteistä ja voi siirtyä reseptiin painamalla sen nimeä. Lisäksi reseptin nimen jälkeen käyttäjä voi poistaa reseptin arkistosta painamalla sen vieressä olevaa poista - painiketta, jolloin resepti poistetaan ohjelman tietokannasta. Hallinnoitisivuilla on myös reseptin lisäämistä tukevia syöttölomakkeita. Lomakkeiden avulla käyttäjä valitsee Reseptille nimen ja painaa Lisää - painiketta lisätäkseen sen tietokantaan. Sivuilla on myös raaka-aineiden lisäykseen liittyvät sopivat lomakkeet. Käyttäjä valitsee ensiksi drop-down - menusta reseptin nimen, ja sen jälkeen drop-down menusta reseptiin lisättävän raaka-aineen nimen. Tämän jälkeen käyttäjä syöttää raaka-aineen lisäysjärjestyksen, määrän ja mahdollisen reseptiohjeen niitä vastaaviin kyselykenttiin, ja painaa lisää - painiketta lisätäkseen raaka-aineen reseptiin. Hallinnointisivuilla on vielä linkki ohjelman pääsivulle.

_____

### Raaka-aineiden Hallinnoitisivu
Raaka-aineiden hallinnointisivulla on listaus kaikista järjestelmän tuntemista raaka-aineista. Jokaista raaka-ainetta vastaa poista - linkki, jonka avulla raaka-aine poistetaan tietokannasta. Samalla raaka-aine poistuu myöskin kaikista niistä resepteistä, joihin se kuuluu. Jokaisella raaka-aineella on myös laskuri, joka kertoo käyttäjälle kuinka monessa tietokantaan tallennetussa reseptissä raaka-aine esiintyy. Sivuilla on myös kyselykenttä, jonka avulla käyttäjä voi syöttää uuden raaka-aineen nimen, ja lisätä sen tietokantaan vastaavalla lisää- painikkeella. Kun raaka-aine on lisätty tietokantaan, se voidaan asettaa eri resepteihin reseptien hallinnointisivulla edellämainitulla tavalla. Hallinnointisivuilla on lopuksi linkki ohjelman pääsivulle.

___

### Raaka-aineiden tarkastelusivu
Jokaisella raaka-aineella on oma tarkastelusivunsa, jossa näytetään kuinka moneen reseptiin kyseinen raaka-aine kuuluu. Sivulta pääsee linkin avulla takaisin raaka-ainelistaukseen.
