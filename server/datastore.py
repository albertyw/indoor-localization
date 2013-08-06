"""
Place to keep data persistence between requests
"""

import os
import json
BASE_DIR = os.path.dirname(os.path.realpath(__file__))

class Datastore:
    STORAGE = os.path.join(BASE_DIR, 'storage.txt')
    def __init__(self):
        self.data = []
        #self.load()

    #def load(self):
    #    handle = open(STORAGE, 'r')
    #    self.data = json.loads(handle.read())
    #    handle.close()

    def save(self):
        handle = open(self.STORAGE, 'w')
        handle.write(json.dumps(self.data))
        handle.close()

    def add(self, new_data):
        self.data.append(new_data)

