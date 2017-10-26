#!/usr/bin/python
# coding=utf-8

import sqlite3 as sql
import sys

if len(sys.argv) > 1:
    db = sys.argv[1]

try:
    db
except:
    sys.exit('No database address.')

connection = sql.connect(db)
cursor = connection.cursor()

statements = [
    'CREATE TABLE Smoothie (id integer PRIMARY KEY, name varchar(20), instructions varchar(1000));',
    'CREATE TABLE Ingredient (id integer PRIMARY KEY, name varchar(20));',
    'CREATE TABLE SmoothieIngredient (smoothie_id integer, raaka_aine_id integer, jarjestys integer, maara varchar(10), FOREIGN KEY(smoothie_id) REFERENCES Smoothie(id), FOREIGN KEY(raaka_aine_id) REFERENCES Ingredient(id));',

    'INSERT INTO Smoothie VALUES(1,\'Hopeinen kuu\',\'Halkaise avokado, poista kivi ja kaavi hedelmäliha lusikalla tehosekoittimeen. Silppua lehtikaali, pilko kurkku ja päärynä ja purista limen mehu sekaan. Surauta. Noin.\');',
    'INSERT INTO Smoothie VALUES(2,\'Bali\',\'Kuori avokado ja mango ja poista kivet. Purista limen mehu. Lisää kaikki ainekset tehosekoittimeen ja surauta tasaiseksi.\');',

    'INSERT INTO Ingredient VALUES(1,\'banaani\');',
    'INSERT INTO Ingredient VALUES(2,\'appelsiini\');',
    'INSERT INTO Ingredient VALUES(3,\'mustikka\');',
    'INSERT INTO Ingredient VALUES(4,\'mansikka\');',
    'INSERT INTO Ingredient VALUES(5,\'avokado\');',
    'INSERT INTO Ingredient VALUES(6,\'lehtikaali\');',
    'INSERT INTO Ingredient VALUES(7,\'mustaviinimarja\');',
    'INSERT INTO Ingredient VALUES(8,\'guava\');',
    'INSERT INTO Ingredient VALUES(9,\'porkkana\');',
    'INSERT INTO Ingredient VALUES(10,\'lime\');',
    'INSERT INTO Ingredient VALUES(11,\'lohikäärmehedelmä\');',
    'INSERT INTO Ingredient VALUES(12,\'kurkku\');',
    'INSERT INTO Ingredient VALUES(13,\'mango\');',
    'INSERT INTO Ingredient VALUES(14,\'päärynä\');',

    'INSERT INTO SmoothieIngredient VALUES(1,5,1,\'1 kpl\');',
    'INSERT INTO SmoothieIngredient VALUES(1,6,2,\'1 kpl\');',
    'INSERT INTO SmoothieIngredient VALUES(1,12,3,\'1/2 kpl\');',
    'INSERT INTO SmoothieIngredient VALUES(1,10,4,\'1 kpl\');',
    'INSERT INTO SmoothieIngredient VALUES(1,14,5,\'1 kpl\');',
    'INSERT INTO SmoothieIngredient VALUES(2,5,1,\'1 kpl\');',
    'INSERT INTO SmoothieIngredient VALUES(2,13,2,\'1 kpl\');',
    'INSERT INTO SmoothieIngredient VALUES(2,4,3,\'5 kpl\');',
    'INSERT INTO SmoothieIngredient VALUES(2,10,4,\'1 kpl\');'
]

for s in statements:
    try:
        cursor.execute(s)
        connection.commit()
    except:
        connection.rollback()

connection.close()
