"""
Flask server to accept data from android phones
"""

import json
from flask import Flask, request
app = Flask(__name__)

from datastore import Datastore
from particle_filter import ParticleFilter
from wifi_magic import WifiMagic

PARTICLE_FILTER = ParticleFilter()

### Server Pages ###

@app.route("/")
def hello():
    return "Localisation 2.0"

@app.route("/push", methods=['GET','POST'])
def data():
    if 'data' not in request.form:
        return 'Nothing received'
    print request.form['data']
    data = json.loads(request.form['data'])

    wifi_magic = WifiMagic()

    for d in data:
        if d['name'] == 'wifi':
            result = wifi_magic.parse(data)

    d = Datastore()
    d.add(data)
    d.save()
    # whoah, it's not ready yet.
    # PARTICLE_FILTER.observe(d.data)
    return 'Saved '+str(data)


if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=80)
    PARTICLE_FILTER = ParticleFilter()
