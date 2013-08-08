"""
Flask server to accept data from android phones
"""

import json
from flask import Flask, request, g

app = Flask(__name__, static_folder='static', static_url_path='/static')

from datastore import Datastore
from particle_filter import ParticleFilter
from wifi_magic import WifiMagic
from sensors_magic import SensorsMagic
from random import sample, random
from werkzeug.contrib.cache import SimpleCache
cache = SimpleCache()

### Server Pages ###

@app.route("/")
def hello():
    handle = open('static/marauders_map.html','r')
    html = handle.read()
    handle.close()
    return html

@app.route("/push", methods=['GET','POST'])
def data():
    if 'data' not in request.form:
        return 'Nothing received'
    data = json.loads(request.form['data'])

    wifi_magic = WifiMagic()

    sensors_magic = SensorsMagic()

    saved_particles = get_db("particles")
    pf = ParticleFilter(particles=saved_particles)

    for d in data:
        if d['name'] == 'sensors':
            result = sensors_magic.parse(d['data'])
            sensors_magic.update_particles(pf.get_particles(), result)
        if d['name'] == 'wifi':
            result = wifi_magic.parse(d['data'])
            result = wifi_magic.update_particles(pf.get_particles(), result)

    pf.resample();
    set_db("particles", pf.get_particles())

    print "Particles updated to", pf.get_position(), " (var:", pf.get_std(),")"
    return 'Saved..'

@app.route("/get")
def get():
    saved_particles = get_db("particles")
    pf = ParticleFilter(particles=saved_particles)
    return 'mean: '+ str(pf.get_position()) + ', std: ' +str(pf.get_std())

@app.route("/sample_particles")
def sample_particles():
    saved_particles = get_db("particles")
    if saved_particles:
        samples = [particle['position'] for particle in sample(saved_particles, 50)]
    else:
        samples = []
    return json.dumps(samples)

@app.route("/check_persistance")
def check_persistance():
    old_shit = get_db("particles")
    print 'old particles:'
    print old_shit
    rand_shit = random()
    print 'going to add: %f' % rand_shit
    save_db(rand_shit)
    return json.dumps((old_shit, rand_shit))

def get_db(name):
    return cache.get(name)

def set_db(name, data):
    cache.set(name, data)
    
@app.route("/router_info")
def send_router_info():
    routers = WifiMagic.ROUTER_POS
    return json.dump(routers)

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=80)
