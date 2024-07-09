import pandas as pd
import numpy as np

# Parameters for the test CSV
num_particles = 3
num_time_steps = 200

# Generate data with larger step sizes to ensure particles are further apart, but within visible frame
np.random.seed(0)  # For reproducibility
step_size = 0.1
x_positions = np.cumsum(np.random.randn(num_particles * num_time_steps) * step_size).reshape(num_time_steps, num_particles)
y_positions = np.cumsum(np.random.randn(num_particles * num_time_steps) * step_size).reshape(num_time_steps, num_particles)
z_positions = np.cumsum(np.random.randn(num_particles * num_time_steps) * step_size).reshape(num_time_steps, num_particles)

# Normalize positions to keep within frame [-5, 5]
x_positions = (x_positions - x_positions.min()) / (x_positions.max() - x_positions.min()) * 10 - 5
y_positions = (y_positions - y_positions.min()) / (y_positions.max() - y_positions.min()) * 10 - 5
z_positions = (z_positions - z_positions.min()) / (z_positions.max() - z_positions.min()) * 10 - 5

data = {
    'time': np.repeat(np.arange(num_time_steps), num_particles),
    'particle_id': np.tile(np.arange(1, num_particles + 1), num_time_steps),
    'x': x_positions.flatten(),
    'y': y_positions.flatten(),
    'z': z_positions.flatten()
}

# Create DataFrame
df = pd.DataFrame(data)

# Save to CSV
df.to_csv('particles_3d_within_frame.csv', index=False)

