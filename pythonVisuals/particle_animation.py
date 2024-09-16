from manim import *
import csv

class ParticleAnimation(Scene):
    def construct(self):
        # Load particle data from CSV
        particle_data = self.load_particle_data('particles.csv')
        
        # Determine number of particles and time steps
        if not particle_data:
            raise ValueError("No data found in CSV file")
        print(particle_data)
        num_particles = (len(particle_data[0]) - 1) // 6
        time_steps = len(particle_data)
        
        # Create dots for particles
        dots = [Dot() for _ in range(num_particles)]
        
        # Initialize dots with their positions from the first time step
        #for i, dot in enumerate(dots):
            #x, y, z = 
            #dot.move_to([x, y, z])
            #self.add(dot)
        
        # Create animations for each time step
        animations = []
        for t in range(time_steps):
            for i in range(num_particles):
                x, y, z = particle_data[t][i*6+1:i*6+4]
                animations.append(
                    UpdateFromFunc(
                        dots[i],
                        lambda m, pos=[x, y, z]: m.move_to(pos)
                    )
                )
        
        # Run the animations
        self.play(*animations, run_time=time_steps, rate_func=linear)

    def load_particle_data(self, filename):
        """Load particle data from a CSV file."""
        data = []
        with open(filename, 'r') as csvfile:
            reader = csv.reader(csvfile, delimiter = ',')
            header = next(reader)  # Skip the header row
            for row in reader:
                try:
                    # Convert row data to float, excluding the time column
                    data.append([float(value) for value in row])
                except ValueError:
                    # Handle cases where conversion might fail
                    print(f"Warning: Skipping row due to conversion error: {row}")
        return data
