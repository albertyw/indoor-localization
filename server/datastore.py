"""
Place to keep data persistence between requests
"""

import os
import json
BASE_DIR = os.path.dirname(os.path.realpath(__file__))

class Datastore:
    STORAGE = BASE_DIR + 'storage.txt'
    def __init__(self):
        handle = open(STORAGE, 'r')
        self.data = handle.read()
        self.data = json.loads(self.data)
        handle.close()

    def save(self):
        handle = open(STORAGE, 'r')
        handle.write(json.dumps(self.data))
        handle.close()

    def add(self, new_data):
        self.data.append(new_data)

