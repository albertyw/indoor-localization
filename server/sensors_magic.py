import math

from random import uniform

class SensorsMagic:
    grid_dim = (5800.0, 780.0)
    PIXELS_PER_METER = 22.6

    dist = 0.4
    vardist = 0.3
    head = 20.0
    varhead = 20.0
    def parse(self, data):
        return data

    def _get_vector(self,angle_deg, dist):
        angle_rad = angle_deg * 2.0 * math.pi / 360.0
        return (dist*math.sin(angle_rad), dist*math.cos(angle_rad))

    def _sanitize(self, particle):
        x,y = particle['position']
        if x < 0 or y < 0 or x > self.grid_dim[0] or y > self.grid_dim[1]:
            particle['weight'] = 0

    def update_particles(self, particles, result):
        for particle in particles:
            dhead = self.head + uniform(-self.varhead,self.varhead)
            ddist = self.dist + uniform(-self.vardist,self.vardist)
            particle['heading'] = (particle['heading'] + dhead) % 360.0
            dx,dy = self._get_vector(particle['heading'], ddist)
            dx *= self.PIXELS_PER_METER
            dy *= self.PIXELS_PER_METER
            x,y = particle['position']
            particle['position'] = (x+dx, y+dy)
            self._sanitize(particle)
        return particles

