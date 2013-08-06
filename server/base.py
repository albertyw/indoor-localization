"""
Flask server to accept data from android phones
"""

import json
import sqlite3
import os
import sys
SERVER_PATH = os.path.dirname(os.path.realpath(__file__))

from flask import Flask, request
app = Flask(__name__)


### Database Stuff ###

DB_PATH = SERVER_PATH + 'db.sqlite'

def connect_db():
    connection = sqlite3.Connection(DB_PATH, timeout=60)
    return connection

def query_db(query, args=(), one=False):
    cur = connect_db().execute(query, args)
    rv = cur.fetchall()
    cur.close()
    return (rv[0] if rv else None) if one else rv


### Server Pages ###

@app.route("/")
def hello():
    return "Localisation 2.0"

@app.route("/data/", methods=['GET','POST'])
def data():
    connection = connect_db()
    if 'data' not in request.form:
        return 'Nothing received'
    data = json.loads(request.form['data'])
    return str(data)


if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
