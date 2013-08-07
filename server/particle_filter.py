from random import uniform
import math

class ParticleFilter:
    num_particles = 1000
    grid_dim = (5800.0, 780.0)

    def __init__(self, particles=None):
        if particles:
            self.particles = particles
        else:
            self.particles = []
            for i in range(self.num_particles):
                particle = {}
                particle['weight'] = 1.0/self.num_particles
                particle['position'] = (uniform(0.0, self.grid_dim[0]),
                                    uniform(0.0, self.grid_dim[1]))
                particle['heading'] = uniform(0.0, 360.0)
                self.particles.append(particle)


    def normalize(self):
        sum = 0.0
        for particle in self.particles:
            sum += particle['weight']
        for particle in self.particles:
            particle['weight'] /= sum

    def resample(self):
        self.normalize();
        prefix_sums = [0.0] * self.num_particles
        for i in range(self.num_particles-1):
            prefix_sums[i+1] = prefix_sums[i] + self.particles[i]['weight']
        sum_choices = [ uniform(0.0, 1.0) for i in range(self.num_particles)]
        sum_choices.sort()


        pointer_particles = 0
        pointer_random = 0
        new_particles = []
        while pointer_random < self.num_particles or pointer_particles<self.num_particles:
            if pointer_particles >= self.num_particles or \
                     (pointer_random < self.num_particles and \
                     prefix_sums[pointer_particles] > \
                     sum_choices[pointer_random]):
                new_particles.append(dict(self.particles[pointer_particles-1]))
                pointer_random += 1
            else:
                pointer_particles += 1
        for particle in new_particles:
            particle['weight'] = 1.0/self.num_particles
        self.particles = new_particles

    def get_particles(self):
        return self.particles

    def set_particles(self, particles):
        self.particles = particles

    def get_position(self):
        sumx, sumy = 0.0, 0.0
        for particle in self.particles:
            sumx += particle['position'][0]
            sumy += particle['position'][1]
        sumx /= self.num_particles
        sumy /= self.num_particles
        return (sumx,sumy)

    def get_std(self):
        stdx, stdy = 0.0, 0.0
        meanx, meany = self.get_position()

        for particles in self.particles:
            stdx += (particles['position'][0] - meanx) ** 2
            stdy += (particles['position'][1] - meany) ** 2
        stdx /= (self.num_particles -1)
        stdy /= (self.num_particles -1)
        stdx = math.sqrt(stdx)
        stdy = math.sqrt(stdy)
        return (stdx, stdy)



