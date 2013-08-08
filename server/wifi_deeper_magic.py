from os import listdir
from os.path import dirname,realpath, isfile, join
import json




class WifiDeeperMagic: # aka router signal correction
    WIFI_FOLDER_NAME = 'wifi_calibration'
    BASE_DIR = dirname(realpath(__file__))
    WIFI_FOLDER_PATH = join(BASE_DIR, WIFI_FOLDER_NAME)

    ASSUMED_BASE_LEVEL = -48.0

    CACHED_NAME = "base_levels"

    def __init__(self, cache): 
        self.cache = cache

    def store_base_level(self, name, base_level):
        handle = open(join(self.WIFI_FOLDER_PATH, name), "w")
        
        handle.write(json.dumps({'l' : base_level}))
        self.cache.delete(self.CACHED_NAME)

    def read_base_levels(self):
        cached = self.cache.get(self.CACHED_NAME)
        if cached:
            return cached
        readings = [ f for f in listdir(self.WIFI_FOLDER_PATH) \
                    if isfile(join(self.WIFI_FOLDER_PATH,f)) ]
        base_levels = {}
        for reading in readings:
            handle = open(join(self.WIFI_FOLDER_PATH,reading),"r")
            level = json.loads(handle.read())['l']
            base_levels[reading] = level
        return base_levels

    def get_corrections(self):
        levels = self.read_base_levels()
        corrections = {}
        for k in levels:
            corrections[k] = self.ASSUMED_BASE_LEVEL - levels[k]
        return corrections
        
