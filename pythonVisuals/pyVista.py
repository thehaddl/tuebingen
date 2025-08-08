import pandas as pd
import numpy as np
import pyvista as pv

data = pd.read_csv("~/git/tuebingen/allparticles.csv")

data['step'] = pd.to_numeric(data['step'], errors='coerce')  # Enforce numeric steps
data['position_x'] = pd.to_numeric(data['position_x'], errors='coerce')
data['position_y'] = pd.to_numeric(data['position_y'], errors='coerce')
data['position_z'] = pd.to_numeric(data['position_z'], errors='coerce')

data.dropna(subset=['step', 'position_x', 'position_y', 'position_z', 'charge'], inplace=True)

timesteps = sorted(data['step'].unique())

positions_by_timestep = {
    step: data[data['step'] == step][['position_x', 'position_y', 'position_z']].values
    for step in timesteps
}



plotter = pv.Plotter()
plotter.add_axes(interactive = True)
plotter.show_axes()
plotter.add_bounding_box(color="black")  # Adds a bounding box that outlines the space



initial_positions = positions_by_timestep[timesteps[0]]

particle_cloud = pv.PolyData(initial_positions)


actor = plotter.add_mesh(particle_cloud, point_size=5.0,color = "black")

def update_time_slider(value):
    timestep_idx = int(round(value))  # Convert slider value to integer
    current_positions = positions_by_timestep[timesteps[timestep_idx]]  # Get current positions
    particle_cloud.points = current_positions  # Update particle positions


plotter.add_slider_widget(
    callback=update_time_slider,
    rng=[0, len(timesteps) - 1],
    value=0,
    title="Timestep",
    style="modern",
    pointa=(0.3, 0.05),
    pointb=(0.7, 0.05), 
    interaction_event="always",
)



# Step 9: Show the interactive plot
plotter.show()
