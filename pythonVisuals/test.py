import pandas as pd
import numpy as np
from manim import *

class ParticleAnimation3D(ThreeDScene):
    def construct(self):
        # Load the data
        data = pd.read_csv("particles_3d_within_frame.csv")
        
        # Get the unique particle ids and the unique time steps
        particle_ids = data['particle_id'].unique()
        time_steps = sorted(data['time'].unique())
        
        # Create axes
        axes = ThreeDAxes()
        self.add(axes)
        
        # Create a dictionary to store particle Mobjects and paths
        particles = {pid: Sphere(radius=0.1).move_to([0, 0, 0]) for pid in particle_ids}
        paths = {pid: VMobject() for pid in particle_ids}
        
        # Initialize paths with the first position
        for pid in particle_ids:
            initial_data = data[(data['time'] == 0) & (data['particle_id'] == pid)]
            if not initial_data.empty:
                initial_position = initial_data[['x', 'y', 'z']].values[0]
                particles[pid].move_to(initial_position)
                paths[pid].set_points_as_corners([initial_position])
                self.add(paths[pid])
        
        # Add particles to the scene
        for particle in particles.values():
            self.add(particle)
        
        # Set up the camera
        self.set_camera_orientation(phi=75 * DEGREES, theta=30 * DEGREES)
        
        # Animate particles over time steps
        for t in time_steps[1:]:
            frame_data = data[data['time'] == t]
            animations = []
            for pid, particle in particles.items():
                row = frame_data[frame_data['particle_id'] == pid]
                if not row.empty:
                    x, y, z = row.iloc[0][['x', 'y', 'z']]
                    new_position = np.array([x, y, z])
                    animations.append(particle.animate.move_to(new_position))
                    if len(paths[pid].points) > 0:
                        paths[pid].add_points_as_corners([particle.get_center(), new_position])
            if animations:  # Only play if there are animations to play
                self.play(*animations, run_time=0.5)  # Increase run_time for slower movement
