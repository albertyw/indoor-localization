"""
Flask server to accept data from android phones
"""

import json
import sqlite3
import os
import sys
server_path = os.path.dirname(os.path.realpath(__file__))

from flask import Flask, request, g
app = Flask(__name__)

database = server_path + 'db.sqlite'

@app.route("/")
def hello():
    return "Localisation 2.0"

@app.route("/data/", methods=['GET','POST'])
def data():
    if 'data' not in request.form:
        return 'Nothing received'
    data = json.loads(request.form['data'])
    return str(data)

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
