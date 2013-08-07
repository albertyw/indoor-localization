"""
Flask server to accept data from android phones
"""

import json
from flask import Flask, request
app = Flask(__name__)

from datastore import Datastore
from particle_filter import ParticleFilter
from wifi_magic import WifiMagic
from sensors_magic import SensorsMagic

### Server Pages ###

@app.route("/")
def hello():
    return "Localisation 2.0"

@app.route("/push", methods=['GET','POST'])
def data():
    if 'data' not in request.form:
        return 'Nothing received'
    data = json.loads(request.form['data'])

    wifi_magic = WifiMagic()

    sensors_magic = SensorsMagic()

    datastore = Datastore()
    saved_particles = datastore.load();
    pf = ParticleFilter(particles=saved_particles)

    for d in data:
        if d['name'] == 'sensors':
            result = sensors_magic.parse(d['data'])
            sensors_magic.update_particles(pf.get_particles(), result)
        if d['name'] == 'wifi':
            result = wifi_magic.parse(d['data'])
            result = wifi_magic.update_particles(pf.get_particles(), result)

    pf.resample();
    datastore.save(pf.get_particles())

    print "Particles updated to", pf.get_position(), " (var:", pf.get_std(),")"
    return 'Saved..'

@app.route("/get")
def get():
    d = Datastore()
    saved_particles = d.load()
    pf = ParticleFilter(particles=saved_particles)
    return 'mean: '+ str(pf.get_position()) + ', std: ' +str(pf.get_std())

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=80)
