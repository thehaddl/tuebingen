import numpy
from manimlib import *
from pyglet.window import key as PygletWindowKeys
class Visuals(Scene):
    def construct(self):

        data = np.genfromtxt(
            "/Users/thaddeusmueller-sarnowski/git/tuebingen/subparticles.csv",
            delimiter=",",
            skip_header=1,
            comments="//",
            dtype=str  
        )
        unique_steps = np.unique(data[:, 0].astype(float))
        num_frames = len(unique_steps)
        
    

        #/////////////////// DOTS ///////////////////////
        all_positions = []
        inuse_indices_per_frame = []
        all_ids = []
        for step in unique_steps:
            step_rows = data[data[:, 0].astype(float) == step]
            positions = step_rows[:, 1:4].astype(float) 
            ids = step_rows[:, 7].astype(int)   
            inuse_mask = step_rows[:, 8] == "true" 
            inuse_indices = np.where(inuse_mask)[0]  
            all_positions.append(positions)
            all_ids.append(ids)
            inuse_indices_per_frame.append(inuse_indices)


        dot_cloud = GlowDots(points=all_positions[0], color=WHITE, radius=0.2)
        dot_cloud.set_glow_factor(0.3)
        self.add(dot_cloud)


        inuse_glow_cloud = GlowDots(points=[], color=YELLOW, radius=0.2, glow_factor=2.0)
        self.add(inuse_glow_cloud)


        time_tracker = ValueTracker(0)


        def highlight_particles(fade_in=True):
            """Smoothly fades in/out the in-use particles at the current frame."""
            current_index = int(time_tracker.get_value()) % num_frames
            inuse_glow_cloud.set_points(all_positions[current_index][inuse_indices_per_frame[current_index]])
            inuse_glow_cloud.refresh_bounding_box()
            

            self.play(
                inuse_glow_cloud.animate.set_opacity(1 if fade_in else 0),
                run_time=0.5,
                rate_func=smooth
            )


        highlight_active = False  

        def toggle_highlight():

            nonlocal highlight_active
            highlight_particles(fade_in=not highlight_active)
            highlight_active = not highlight_active

    


        def update_clouds(mob, dt):

            current_index = int(time_tracker.get_value()) % num_frames
            dot_cloud.set_points(all_positions[current_index])
            dot_cloud.refresh_bounding_box()


            if highlight_active:
                inuse_glow_cloud.set_points(all_positions[current_index][inuse_indices_per_frame[current_index]])
                inuse_glow_cloud.refresh_bounding_box()

        dot_cloud.add_updater(update_clouds)
        #dot_cloud.make_3d()
        inuse_glow_cloud.add_updater(update_clouds)

        #/////////////////// LABLES ///////////////////////

        def create_particle_labels():
            particle_labels = VGroup()
            positions = all_positions[0]
            ids = all_ids[0]
            for i, pid in enumerate(ids):
                label = Text(str(pid), font_size=12)

                label.set_color(BLUE)
                label.move_to(positions[i])
                particle_labels.add(label)
            return particle_labels

        particle_labels = create_particle_labels()
        self.add(particle_labels)

        def update_particle_labels(mob, dt):
            current_index = int(time_tracker.get_value()) % num_frames
            positions = all_positions[current_index]
            
            for i, label in enumerate(mob):
                if i < len(positions):
                    label.move_to(positions[i])
                    

        particle_labels.add_updater(update_particle_labels)

        labels_visible = False

        def toggle_labels():
            nonlocal labels_visible
            labels_visible = not labels_visible 
            if labels_visible:
                self.play(particle_labels.animate.set_opacity(0), run_time=0.5)

            else:
                self.play(particle_labels.animate.set_opacity(1), run_time=0.5)



        #/////////////////// VELOCITY VECTORS ///////////////////////
        fraction_of_arrows = 0.1 
        all_velocities = []
        num_particles = len(all_positions[0])
        num_arrows = max(1, int(fraction_of_arrows * num_particles))
        for step in unique_steps:
            step_rows = data[data[:, 0].astype(float) == step]
            velocities = step_rows[:, 4:7].astype(float)
            all_velocities.append(velocities)

        selected_indices = np.random.choice(num_particles, num_arrows, replace=False)
        initial_positions = all_positions[0][selected_indices]
        initial_velocities = all_velocities[0][selected_indices]
        scaling_factor = 0.5  
        velocity_arrows = VGroup(*[
            Arrow(
                start=pos,
                end=pos + scaling_factor * vel,
                buff=0,
                stroke_width=0.3,
                thickness = 0.6,
                color=BLUE
            )
            for pos, vel in zip(initial_positions, initial_velocities)
        ])
        self.add(velocity_arrows)

        def set_fraction(a):
            if a <= 1 and a >0:
                fraction_of_arrows = a
            else:
                print("invalid fraction")
    
        def update_velocity_arrows(mob, dt):
            current_index = int(time_tracker.get_value()) % num_frames
            positions = all_positions[current_index][selected_indices]
            velocities = all_velocities[current_index][selected_indices]
            if len(mob) != len(positions):
                new_arrows = VGroup(*[
                    Arrow(
                        start=pos,
                        end=pos + scaling_factor * vel,
                        buff=0,
                        stroke_width=0.3,
                        thickness = 0.6,
                        color=RED
                    )
                    for pos, vel in zip(positions, velocities)
                ])
                mob.become(new_arrows)
            else:
                for arrow, pos, vel in zip(mob, positions, velocities):
                    arrow.put_start_and_end_on(pos, pos + scaling_factor * vel)

        velocity_arrows_visible = False
        velocity_arrows.add_updater(update_velocity_arrows)

        def toggle_velocity_arrows():
            nonlocal velocity_arrows_visible
            if velocity_arrows_visible:
                self.play(velocity_arrows.animate.set_opacity(0), run_time=0.5)  # Smooth fade out
                self.remove(velocity_arrows) 
            else:
                self.add(velocity_arrows) 
                self.play(velocity_arrows.animate.set_opacity(1), run_time=0.5)  # Smooth fade in
            velocity_arrows_visible = not velocity_arrows_visible


        #/////////////////// LINES ///////////////////////
        line_group = VGroup()
        lines_visible = False 
        opacity = 0.7
        def toggle_lines():
            nonlocal lines_visible, line_group
            colors = [RED,BLUE,GREEN]

            if lines_visible:
                
                self.remove(line_group)
                line_group = VGroup()
                lines_visible = False
            else:
                current_index = int(time_tracker.get_value()) % num_frames
                positions = all_positions[current_index]
                inuse_indices = inuse_indices_per_frame[current_index]

                if len(inuse_indices) == 0:
                    print("no points inuse")
                    return 

                random_particle = random.choice(range(len(positions)))

                new_lines = VGroup(*[
                    Line(positions[random_particle], positions[inuse], color=random.choice(colors))
                    for inuse in inuse_indices
                ])
                for l in new_lines:
                    l.set_opacity(opacity)
                

                line_group = new_lines
                self.add(line_group)
                lines_visible = True

        def toggle_all_lines():
            colors = [RED,BLUE,GREEN]
            nonlocal lines_visible, line_group

            if lines_visible:

                self.remove(line_group)
                line_group = VGroup()
                lines_visible = False
            else:

                current_index = int(time_tracker.get_value()) % num_frames
                positions = all_positions[current_index]
                inuse_indices = inuse_indices_per_frame[current_index]

                if len(inuse_indices) == 0:
                    return

                new_lines = VGroup(*[
                    Line(positions[p], positions[inuse], color=random.choice(colors), stroke_width=0.5)
                    for p in range(len(positions))
                    for inuse in inuse_indices
                ])
                for l in new_lines:
                    l.set_opacity(opacity)
                line_group = new_lines
                self.add(line_group)
                lines_visible = True
    

        connection_lines = VGroup()
        self.add(connection_lines)

        def sequentially_highlight_lines(nth):
 
            connection_lines.clear()
            current_index = int(time_tracker.get_value()) % num_frames
            positions = all_positions[current_index]
            inuse_indices = inuse_indices_per_frame[current_index]
            

            if len(positions) == 0 or len(inuse_indices) == 0:
                return  

            ids = all_ids[current_index]
            selected_particles = [i for i, id in enumerate(ids) if id % nth == 0]

            new_lines = VGroup()
            for selected_particle in selected_particles:
                random_particle = selected_particle  # Particle for which to create lines
                for inuse in inuse_indices:
                    line = Line(positions[random_particle], positions[inuse], color=YELLOW, stroke_width=0.5)
                    new_lines.add(line)
                
                self.play(
                    new_lines.animate.set_opacity(1), 
                    run_time=0.2
                )
                self.wait(0.05)  
                self.play(
                    new_lines.animate.set_opacity(0), 
                    run_time=0.2
                )
                new_lines.clear()  


        #/////////////////// CAMERA TRACKING A PARTICLE ///////////////////////
        selected_particle_id = 1  
        scaling_factor = 1        

        
        initial_ids = all_ids[0]
        selected_index = None
        for i, pid in enumerate(initial_ids):
            if pid == selected_particle_id:
                selected_index = i
                break

        if selected_index is None:
            raise ValueError("Selected particle ID not found in the first frame!")

        initial_pos = all_positions[0][selected_index]
        initial_vel = all_velocities[0][selected_index]

        selected_arrow = Arrow(
            start=initial_pos,
            end=initial_pos + scaling_factor * initial_vel,
            color=RED,
            thickness = 0.6,
            stroke_width=0.3
        )
        self.add(selected_arrow)
        def update_selected_arrow(mob, dt):
            current_index = int(time_tracker.get_value()) % num_frames
            positions = all_positions[current_index]
            velocities = all_velocities[current_index]
            ids = all_ids[current_index]
            
            selected_idx = None
            for i, pid in enumerate(ids):
                if pid == selected_particle_id:
                    selected_idx = i
                    break
            if selected_idx is not None:
                pos = positions[selected_idx]
                vel = velocities[selected_idx]
                mob.put_start_and_end_on(pos, pos + scaling_factor * vel)


        camera_following = False 
        

        initial_positions = all_positions[0][selected_indices]
        initial_velocities = all_velocities[0][selected_indices]
        def set_particle_id(index):
            nonlocal selected_particle_id
            selected_particle_id = index

        def update_camera(mob, dt):
            if camera_following:
                current_index = int(time_tracker.get_value()) % num_frames
                positions = all_positions[current_index]


                particle_position = None
                for i, pid in enumerate(all_ids[current_index]):
                    if pid == selected_particle_id:
                        particle_position = positions[i]
                        break

                
                if particle_position is not None:
                    '''bounding_box = dot_cloud.compute_bounding_box()
                    center = bounding_box[1]  # Mittelpunkt der Bounding Box
                    camera_pos = self.camera.frame.get_center()  # Aktuelle Kameraposition

                    
                    direction = center - camera_pos
                    direction /= np.linalg.norm(direction) 

                    t = np.arcsin(direction[2])  # Höhe (Z-Koordinate)
                    p = np.arctan2(direction[1], direction[0])  # Rotation um Z-Achse

                    
                    self.camera.frame.set_euler_angles(theta=t, phi=p)'''
                    #self.camera.frame.set_width(5) 

                    offset = particle_position - self.camera.frame.get_center()
                    self.camera.frame.shift(offset)




        def follow_particle(index):
            set_particle_id(index)
            camera_following = True
            self.add(selected_arrow)
            self.play(FadeIn(selected_arrow))
            selected_arrow.add_updater(update_selected_arrow)
            self.camera.frame.add_updater(update_camera)

        def toggle_camera_follow():
            nonlocal camera_following
            if camera_following:
                selected_arrow.remove_updater(update_selected_arrow)
                self.play(FadeOut(selected_arrow))
                

            else:
                selected_arrow.add_updater(update_selected_arrow)
                self.play(FadeIn(selected_arrow))
                
                
            camera_following = not camera_following
        #/////////////////// INTERACTIVE TOOLBOX EXTRAS ///////////////////////


        #Lines
        def switch_lines():
            toggle_lines()
            toggle_lines()

        # ValueTracker
        def sim(relative_time,duration= None):
            if duration is None:
                self.play(time_tracker.animate.set_value((relative_time/100)*(num_frames-1)),rate_func=linear,run_time=15)
            else:
                self.play(time_tracker.animate.set_value((relative_time/100)*(num_frames-1)),rate_func=linear,run_time=duration)
        
        def play(speed):
            
            run_time = (num_frames - 1) / speed
            self.play(time_tracker.animate.set_value(num_frames - 1),
                      rate_func=linear, run_time=run_time)
            self.wait(2)
            time_tracker.set_value(0)

        def reset():
            time_tracker.set_value(0)

        def set_time(relative_time):
            time_tracker.set_value((relative_time/100)*(num_frames-1))
        
     
        #//////////////// HELPERS ////////////////

        #ValueTracker
        def r():
            reset()   


        def p(speed=None):
            if speed:
                play(speed)
            else:
                play(50)
        def st(relative_time):
            set_time(relative_time)

        #Vectors
        def tv():
            toggle_velocity_arrows()

        #Dots
        def th():
            toggle_highlight()

        #Lines
        def tl():
            toggle_lines()

        def tal():
            toggle_all_lines()

        def sl():
            switch_lines()
        def sqh(nth = None):
            if nth:
                sequentially_highlight_lines(nth)
            else:
                sequentially_highlight_lines(10)

        #Camera
        def tcf():
            toggle_camera_follow()
        def follow(i):
            follow_particle(i)
        
        #adjust before embed
        


        #//////////////// CHOREOGRAPHIES ////////////////
        theta = ValueTracker(0) 
        phi = ValueTracker(0) 
        gamma = ValueTracker(0) 
        frame = self.camera.frame
        def angle_updater(mob,dt):
            frame.set_euler_angles(theta = theta.get_value()*DEGREES,phi = phi.get_value()*DEGREES,gamma = gamma.get_value()*DEGREES)

        def choreo(t,p,g,zoom,runtime):
            frame.set_width(4)
            frame.add_updater(angle_updater)
            frame.add_updater(look_at_middle)
            self.play(
                theta.animate.set_value(t),
                phi.animate.set_value(p),
                gamma.animate.set_value(g),
                frame.animate.set_width(zoom),
                time_tracker.animate.set_value(100),
                run_time = runtime,
                rate_func = linear
            )
            frame.remove_updater(angle_updater)

        def continuous_drive(time,zoom = None):
            def rotate_frame(m, dt):
                m.increment_theta(-0.1 * dt)
                m.increment_phi(-0.1 * dt)
                m.increment_gamma(-0.1 * dt)
            frame.add_updater(rotate_frame)
            if zoom:
                self.play(frame.animate.set_width(zoom))
            else:
                self.wait(time)

        def look_at_middle(mob,dt):
            bounding_box = dot_cloud.compute_bounding_box()
            center = bounding_box[1]  # Mittelpunkt der Bounding Box
            camera_pos = self.camera.frame.get_center()  # Aktuelle Kameraposition

            
            direction = center - camera_pos
            direction /= np.linalg.norm(direction) 

            t = np.arcsin(direction[2])  # Höhe (Z-Koordinate)
            p = np.arctan2(direction[1], direction[0])  # Rotation um Z-Achse

            
            self.camera.frame.set_euler_angles(theta=t, phi=p)      
        

            
        self.camera.frame.set_width(4)  
        #choreo(45,45,45,10,20)
        #continuous_drive(10,5)
        #self.play(self.camera.frame.set_width(1900), run_time=25, rate_func=smooth)
        #self.camera.frame.set_width(1900)
        #image = self.get_image()
        #image.show()

        self.embed()

'''self.play(theta.animate.set_value(0),phi.animate.set_value(0),gamma.animate.set_value(0)
 ,run_time = 10)
 self.play(theta.animate.set_value(100),phi.animate.set_value(100),gamma.animate.set_value(100)
 ,run_time = 10)
self.play(theta.animate.set_value(0),
 run_time = 10)
self.play(phi.animate.set_value(0),
 run_time = 10)
self.play(gamma.animate.set_value(0),
 run_time = 10)
self.play(theta.animate.set_value(100),
 run_time = 10)
self.play(phi.animate.set_value(100),
 run_time = 10)
self.play(gamma.animate.set_value(100),
 run_time = 10)'''