import pandas as pd
from manim import *
config.disable_caching = True
def readData():
    df = pd.read_csv('allparticles.csv', header= None)
    df = df.dropna(axis=1, how='all')
    columns = ['step','x1', 'x2', 'x3', 'vx', 'vy', 'vz']  # Define column names based on your data
    df.columns = columns
    return df
df = readData()
scale_factor = 1000
print(df.head())

class Animation(ThreeDScene):
    def construct(self):
        num_particles = 30
        num_time_steps = len(df) // num_particles
        particle_radius = 0.07
        particles = [Sphere(radius=particle_radius, color=BLUE) for _ in range(num_particles)]
        axes = ThreeDAxes(
            x_range=[-1*scale_factor, 1*scale_factor, 20],  # x-axis range from -100 to 100 with tick marks every 20 units
            y_range=[-1*scale_factor, 1*scale_factor, 20],  # y-axis range from -100 to 100 with tick marks every 20 units
            z_range=[-1*scale_factor, 1*scale_factor, 20],  # z-axis range from -100 to 100 with tick marks every 20 units
        )

        self.add(axes)
        self.set_camera_orientation(phi=75 * DEGREES, theta=30 * DEGREES)

        for i, particle in enumerate(particles):
            initial_pos = [df.iloc[i]['x1'],
                           df.iloc[i]['x2'],
                           df.iloc[i]['x3']]
            particle.move_to(initial_pos)
            self.add(particle)


        for t in range(num_time_steps):
            animations = []

            for i, particle in enumerate(particles):
                row_index = i + t * num_particles
                if row_index < len(df):
                    new_pos = [df.iloc[row_index]['x1'],
                               df.iloc[row_index]['x2'],
                               df.iloc[row_index]['x3']]

                    print(f"Particle {i} - Time Step {t}: New Position: {new_pos}")
                    animations.append(particle.animate.move_to(new_pos))

            self.play(*animations, run_time=0.1, rate_func = smooth)
            self.wait(0.1)

        self.wait(2)