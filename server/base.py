"""
Flask server to accept data from android phones
"""

import json
from flask import Flask, request
app = Flask(__name__)

from datastore import Datastore

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
    d = Datastore()
    d.add(data)
    d.save()
    return 'Saved '+str(data)


if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
