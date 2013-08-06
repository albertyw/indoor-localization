from random import uniform

class ParticleFilter:
    
    num_particles = 10000
    particles = [{}] * num_particles
    grid_dim = (200, 200)

    def __init__(self):
        for particle in self.particles:
            particle['weight'] = 1.0/self.num_particles
            particle['position'] = (uniform(0, self.grid_dim[0]),
                                    uniform(0, self.grid_dim[1]))
            particle['heading'] = uniform(0, 360)
            
    
    def observe(self, observation):
        # consume observation
        self.normalize()
        self.resample()

    def normalize(self):
        sum = 0.0
        for particle in self.particles:
            sum += particle['weight']
        for particle in self.particles:
            particle['weight'] /= sum

    def resample(self):
        prefix_sums = [0.0] * self.num_particles
        for i in range(self.num_particles-1):
            prefix_sums[i+1] = prefix_sums[i] + self.particles[i]['weight']
        sum_choices = [ uniform(0.0, 1.0) for i in range(self.num_particles)]
        pointer_particles = 0
        pointer_random = 0
        new_particles = []
        while pointer_random < self.num_particles or pointer_particles<self.num_particles:
            if pointer_particles >= self.num_particles or \
                     (pointer_random < self.num_particles and \
                     self.particles[pointer_particles]['weight'] > \
                     sum_choices[pointer_random]):
                new_particles.append(dict(self.particles[pointer_particles-1]))
                pointer_random += 1
            else:
                pointer_particles += 1
        for particle in new_particles:
            particle['weight'] = 1.0/self.num_particles
        self.particles = new_particles
        
    def actuate(self, action):
        # consume action
        pass

