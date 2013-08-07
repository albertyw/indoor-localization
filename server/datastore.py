"""
Place to keep data persistence between requests
"""

import os
import json
BASE_DIR = os.path.dirname(os.path.realpath(__file__))

class Datastore:
    STORAGE = os.path.join(BASE_DIR, 'storage.txt')

    def load(self):
        try:
            handle = open(self.STORAGE, 'r')
        except IOError:
            return None
        data = json.loads(handle.read())
        handle.close()
        return data

    def save(self, data):
        handle = open(self.STORAGE, 'w')
        handle.write(json.dumps(data))
        handle.close()
