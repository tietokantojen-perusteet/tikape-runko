#!/bin/sh

FILENAME=smoothietietokanta

if [ $# -eq 1 ]; then
    FILENAME=$1;
fi

if [ -e $FILENAME.db ]; then
    rm $FILENAME.db;
fi

touch $FILENAME.db;
python db.py $FILENAME.db;
