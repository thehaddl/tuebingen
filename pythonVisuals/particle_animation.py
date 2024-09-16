import pandas as pd
from manim import *

def readData():
    df_temp = pd.read_csv('particles.csv', nrows=0)
    columns_to_read = df_temp.columns[:-1]
    d = pd.read_csv('particles.csv', usecols=columns_to_read)
    return d
df = readData()
scale_factor = 10
print(df.head())

class Animation(ThreeDScene):
    def construct(self):
        num_particles = 5
        num_time_steps = len(df) // num_particles
        particle_radius = 0.1 
        particles = [Sphere(radius=particle_radius, color=BLUE) for _ in range(num_particles)]
        axes = ThreeDAxes()
        self.add(axes)
        self.set_camera_orientation(phi=75 * DEGREES, theta=30 * DEGREES)

        for i, particle in enumerate(particles):
            initial_pos = [df.iloc[i][0],
                           df.iloc[i][1],
                           df.iloc[i][2]]
            particle.move_to(initial_pos)
            self.add(particle)


        for t in range(num_time_steps):
            animations = []

            for i, particle in enumerate(particles):
                row_index = i + t * num_particles
                if row_index < len(df):
                    new_pos = [df.iloc[row_index][0],
                               df.iloc[row_index][1],
                               df.iloc[row_index][2]]

                    print(f"Particle {i} - Time Step {t}: New Position: {new_pos}")
                    animations.append(particle.animate.move_to(new_pos))

            self.play(*animations, run_time=0.4, rate_func = smooth)
            self.wait(0.1)

        self.wait(2)