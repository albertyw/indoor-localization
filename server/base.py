"""
Flask server to accept data from android phones
"""

import json
from flask import Flask, request
app = Flask(__name__)

from datastore import Datastore
from particle_filter import ParticleFilter

PARTICLE_FILTER = ParticleFilter()

### Server Pages ###

@app.route("/")
def hello():
    return "Localisation 2.0"

@app.route("/data/", methods=['GET','POST'])
def data():
    if 'data' not in request.form:
        return 'Nothing received'
    data = json.loads(request.form['data'])
    d = Datastore()
    d.add(data)
    d.save()
    PARTICLE_FILTER.observe(d.data)
    return 'Saved '+str(data)


if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
    PARTICLE_FILTER = ParticleFilter()
