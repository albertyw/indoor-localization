import math

class WifiMagic:
    PIXELS_PER_METER = 22.6

    LOG_MIN_PROB = -20
    MIN_DISTANCE_STD_M = 1
    NORM_Z = math.log(0.39894)

    ROUTER_POS = {

        # Part 1
        "ROUTER_NAME" : (100, 200),
    }

    ROUTER_HEIGHT = 2.5

    ROUTER_OFFSET = 0.0

    KEEP_X_ROUTERS = 0

    LIGHT = 299792458.0;

    SMALL_RESULT_AMPLIFY = 0.7

    # N_COEFFS = [-0.07192023, -2.40415772]
    N_COEFFS = [-0.07363796, -2.52218124]


    def parse(self, data):
        res = []

        for router in data:
            router['estimatedDistance'] = \
            self.get_distance_from_level(router['level'], 1000000.0*router['freqMhz'])

        # discard all the routers but the top X with lowest readings
        if self.KEEP_X_ROUTERS > 0:
            newdata = sorted(data, key=lambda k: k['estimatedDistance'])
            data = newdata[:self.KEEP_X_ROUTERS]


        for router in data:
            if router['label'] in self.ROUTER_POS:
                new_distance = float(router['estimatedDistance'])
                new_distance = max(self.MIN_DISTANCE_STD_M, \
                                        new_distance - self.ROUTER_OFFSET)
                rd = (self.ROUTER_POS[router['label']], new_distance)
                res.append(rd)
        return res

    def lin_eval(self,coeffs, x):
        a, b = coeffs
        return a * x + b

    def compute_N(self,level):
        return max(2, self.lin_eval(self.N_COEFFS, level))

    def get_distance_from_level(self,level, frequencyHz):
        n = self.compute_N(level)

        # from wikipedia
        level = -level

        wavelength = self.LIGHT/frequencyHz

        C = 20.0 * math.log(4.0 * math.pi / wavelength, 10)
        r_in_meters = 10 ** ((level - C) / (10.0 * n))

        r_in_meters = max(self.ROUTER_HEIGHT, r_in_meters)
        dist_in_meters = math.sqrt(r_in_meters ** 2 - self.ROUTER_HEIGHT ** 2)
        return dist_in_meters

    def update_particles(self, particles, router_distances):
        for particle in particles:
            log_probs = list(self.distance_observation_probability(
                            router_distances, particle['position']))
            probs = map(lambda x : math.e ** x, log_probs)
            probs.sort()
            probs = probs[-4:]

            particle['weight'] *= reduce(lambda x,y:x*y,probs)
        return particles

    def loglikelihood(self, x):
        return self.NORM_Z - 0.5*x*x

    def distance_observation_probability(self,router_distances, xy):
        ll = 0
        x, y = xy
        pixels_sq = self.PIXELS_PER_METER ** 2

        for (x1, y1), distanceMeasuredM in router_distances:
            distancePredictedM = math.sqrt((x - x1)**2/pixels_sq + (y - y1)**2 / pixels_sq)
            yield max(self.LOG_MIN_PROB,
                      self.loglikelihood((distanceMeasuredM - distancePredictedM) /
                      max(self.MIN_DISTANCE_STD_M, distanceMeasuredM**1.6/10.0)))


