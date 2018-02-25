# Lent�v�n virtahevon reseptiarkisto

###Teht�v�n tavoite
Projektin tavoitteena on tehd� verkkopohjainen reseptiarkistosovellus.
Ohjelman toiminta perustuu tietokantaan, johon tallennetaan k�ytt�j�n sinne valitsemia Ruoka-, tai juomaohjeita. Sen k�ytt�liittym� on yksinkertainen selaimen tulkitseva verkkosivu, jonka esitt�minen toteutetaan java-pohjaisena ohjelmana. Ohjelma tallentaa k�ytt�j�n antamat reseptit java-toteutuksen avulla itse tietokantaan, ja noutaa k�ytt�j�n pyyt�m�t tiedot tietokannasta my�skin java-rajapinnan kautta.

___

###Ohjelma koostuu seuraavista osista:
1. SQL-pohjainen relaatiotietokanta, johon tieto tallenetaan.
2. Javaohjelma, joka sis�lt�� tietokantaa vastaavat olioluokat, tietokanta-abstraktion ja DatabaseAccessObject-luokat.
3. HTML-verkkosivut, joiden ulkoasu tulkitaan k�ytt�j�n valitsemalla selaimella.

___

###Ohjelmakuvaus

####P��sivu
Avatessaan ohjelman p��sivun, k�ytt�j� n�kee arkiston nimen, ja listauksen ohjelmaan tallennetuista resepteist�. K�ytt�j� voi valita mink� tahansa listatuista resepteist� painamalla sen nime� hiirell�, mik� avaa resepti� vastaavan reseptisivun selaimessa. Vaihtoehtoisesti k�ytt�j� voi avata uuden reseptin hallinnointisivun, tai reseptien raaka-aineiden tarkastelu- ja hallinnointisivun.
___

####Reseptisivu
Avatessaan reseptisivun, k�ytt�j� n�kee reseptin nimen, sek� listauksen reseptin raaka-aineista, niiden kappalem��rist� ja lis�ysj�rjestyksist�. Reseptiin voi my�s liitty� erillinen ohje, joka my�s n�ytet��n k�ytt�j�lle. Halutessaan, k�ytt�j� p��see takaisin ohjelman p��sivulle painamalla sivulla olevaa linkki�.
____

####Reseptien Hallinnoitisivu
Uuden reseptin hallinnointisivulla k�ytt�j� n�kee listauksen resepteist� ja voi siirty� reseptiin painamalla sen nime�. Lis�ksi reseptin nimen j�lkeen k�ytt�j� voi poistaa reseptin arkistosta painamalla sen vieress� olevaa poista - painiketta, jolloin resepti poistetaan ohjelman tietokannasta. Hallinnoitisivuilla on my�s reseptin lis��mist� tukevia sy�tt�lomakkeita. Lomakkeiden avulla k�ytt�j� valitsee Reseptille nimen ja painaa Lis�� - painiketta lis�t�kseen sen tietokantaan. Sivuilla on my�s raaka-aineiden lis�ykseen liittyv�t sopivat lomakkeet. K�ytt�j� valitsee ensiksi drop-down - menusta reseptin nimen, ja sen j�lkeen drop-down menusta reseptiin lis�tt�v�n raaka-aineen nimen. T�m�n j�lkeen k�ytt�j� sy�tt�� raaka-aineen lis�ysj�rjestyksen, m��r�n ja mahdollisen reseptiohjeen niit� vastaaviin kyselykenttiin, ja painaa lis�� - painiketta lis�t�kseen raaka-aineen reseptiin. Hallinnointisivuilla on viel� linkki ohjelman p��sivulle.

_____

####Raaka-aineiden Hallinnoitisivu
Raaka-aineiden hallinnointisivulla on listaus kaikista j�rjestelm�n tuntemista raaka-aineista. Jokaista raaka-ainetta vastaa poista - linkki, jonka avulla raaka-aine poistetaan tietokannasta. Samalla raaka-aine poistuu my�skin kaikista niist� resepteist�, joihin se kuuluu. Jokaisella raaka-aineella on my�s laskuri, joka kertoo k�ytt�j�lle kuinka monessa tietokantaan tallennetussa reseptiss� raaka-aine esiintyy. Sivuilla on my�s kyselykentt�, jonka avulla k�ytt�j� voi sy�tt�� uuden raaka-aineen nimen, ja lis�t� sen tietokantaan vastaavalla lis��- painikkeella. Kun raaka-aine on lis�tty tietokantaan, se voidaan asettaa eri resepteihin reseptien hallinnointisivulla edell�mainitulla tavalla. Hallinnointisivuilla on lopuksi linkki ohjelman p��sivulle.

