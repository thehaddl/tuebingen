from manim import *
import numpy as np
import pandas as pd

class ParticleSimulation(Scene):
    def construct(self):
        num_particles = 1000
        num_steps = 10000
        
        # Load position data from CSV
        positions_all_interactions = pd.read_csv("~/IdeaProjects/T/positions_all_interactions.csv", header=None).values
        positions_random_interactions = pd.read_csv("~/IdeaProjects/T/positions_random_interactions.csv", header=None).values

        # Create Mobjects for particles
        particles_all = [Dot(point=[0, 0, 0], color=BLUE) for _ in range(num_particles)]
        particles_random = [Dot(point=[0, 0, 0], color=RED) for _ in range(num_particles)]
        
        particles_all_group = VGroup(*particles_all)
        particles_random_group = VGroup(*particles_random)
        
        self.add(particles_all_group, particles_random_group)
        
        # Animation loop
        for step in range(num_steps):
            # Update particle positions for all interactions
            for i in range(num_particles):
                pos_all = positions_all_interactions[step*num_particles + i]
                particles_all[i].move_to([pos_all[0], pos_all[1], pos_all[2], 0])

            # Update particle positions for random interactions
            for i in range(num_particles):
                pos_random = positions_random_interactions[step*num_particles + i]
                particles_random[i].move_to([pos_random[0], pos_random[1], pos_random[2], 0])
            
            self.wait(0.01)  # Adjust the wait time for animation speed

# To render the scene, run:
# manim -pql your_manim_script.py ParticleSimulation
