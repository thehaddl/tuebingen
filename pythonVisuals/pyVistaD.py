import pandas as pd
import numpy as np
import pyvista as pv


#data = pd.read_csv("~/git/tuebingen/allparticles.csv")
#data2 = pd.read_csv("~/git/tuebingen/subparticles.csv")
#data = pd.read_csv("~/git/tuebingen/allparticlesDemo1.csv")
#data2 = pd.read_csv("~/git/tuebingen/subparticlesDemo1.csv")
data = pd.read_csv("~/git/tuebingen/allparticlesDemo2.csv")
data2 = pd.read_csv("~/git/tuebingen/subparticlesDemo2.csv")

for df in [data, data2]:
    df['step'] = pd.to_numeric(df['step'], errors='coerce')
    df['position_x'] = pd.to_numeric(df['position_x'], errors='coerce')
    df['position_y'] = pd.to_numeric(df['position_y'], errors='coerce')
    df['position_z'] = pd.to_numeric(df['position_z'], errors='coerce')
    df.dropna(subset=['step', 'position_x', 'position_y', 'position_z'], inplace=True)

timesteps = sorted(data['step'].unique())

positions_by_timestep = {
    step: data[data['step'] == step][['position_x', 'position_y', 'position_z']].values
    for step in timesteps
}
positions_by_timestep2 = {
    step: data2[data2['step'] == step][['position_x', 'position_y', 'position_z']].values
    for step in timesteps
}


plotter = pv.Plotter()
plotter.add_axes(interactive=True)
plotter.show_axes()
plotter.add_bounding_box(color="black")


initial_positions = positions_by_timestep[timesteps[0]]
initial_positions2 = positions_by_timestep2[timesteps[0]]

particle_cloud = pv.PolyData(initial_positions)
particle_cloud2 = pv.PolyData(initial_positions2)

actor = plotter.add_mesh(particle_cloud, point_size=5.0, color="black")
actor2 = plotter.add_mesh(particle_cloud2, point_size=5.0, color="red")


def create_connection_lines(points1, points2):
    lines_points = np.vstack([points1, points2])
    num_points = len(points1)
    lines = []
    for i in range(num_points):
        start_idx = i
        end_idx = i + num_points
        lines.extend([2, start_idx, end_idx])  # '2' indicates a line with two points
    poly = pv.PolyData()
    poly.points = lines_points
    poly.lines = lines
    return poly

connection_lines = create_connection_lines(initial_positions, initial_positions2)
line_actor = plotter.add_mesh(connection_lines, color="blue", line_width=1.5)


def update_time_slider(value):
    timestep_idx = int(round(value))
    current_positions = positions_by_timestep[timesteps[timestep_idx]]
    current_positions2 = positions_by_timestep2[timesteps[timestep_idx]]

    particle_cloud.points = current_positions
    particle_cloud2.points = current_positions2

    new_lines = create_connection_lines(current_positions, current_positions2)
    connection_lines.points = new_lines.points
    connection_lines.lines = new_lines.lines


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


plotter.show()
