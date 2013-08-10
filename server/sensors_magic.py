import math

from random import uniform

class SensorsMagic:
    grid_dim = (5800.0, 780.0)
    PIXELS_PER_METER = 22.6

    dist = 0.3
    vardist = 0.3
    head = 0.0
    varhead = 2.5
    
    USE_WALLS = True

    TICKS = 5

    def __init__(self, walls=None):
        self.walls = walls

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
            dhead = self.head + uniform(-self.varhead,self.varhead) + result['dheading']
            ddist = self.dist + uniform(-self.vardist,self.vardist)
            particle['heading'] = (particle['heading'] + dhead) % 360.0
            dx,dy = self._get_vector(particle['heading'], ddist)
            dx *= self.PIXELS_PER_METER
            dy *= self.PIXELS_PER_METER
            (x1,y1) = particle['position']
            (x2,y2) = (x1+dx, y1+dy)
            if self.USE_WALLS and not self.is_move_legal(x1, y1, x2, y2):
                particle['weight'] = 0
            particle['position'] = (x2, y2)
            self._sanitize(particle)
        return particles

    def is_move_legal(self, x1, y1, x2, y2):
        if not self.walls:
            return True
        for i in range(self.TICKS):
            to_check = (round((i + 1.0) * (x2 - x1)/self.TICKS + x1),
                        round((i + 1.0) * (y2 - y1)/self.TICKS + y1))
            if to_check in self.walls:
                return False
        return True
            
