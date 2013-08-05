import json

from flask import Flask
from flask import request
app = Flask(__name__)

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
