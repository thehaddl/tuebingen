import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

class Simulation {

    public double dimension;
    private double runTime = 100;
    final Particle[] initialParticles;
    private final int steps;
    private final double dt;

    private SimStepOutput output = SimStepOutput.NOP;

    public Simulation(int numP, int s, double runTimem, double dimension) {
        this.dimension = dimension;
        steps = s;
        this.runTime = runTime;
        initialParticles = new Particle[numP];
        dt = runTime/steps;
        Random r = new Random();
        for (int i = 0; i < numP; i++) {
            Vector vel = new Vector(0,0,0);
            Vector pos = new Vector(r.nextDouble()* dimension, r.nextDouble()* dimension, r.nextDouble()* dimension);
            initialParticles[i] = new Particle(pos, vel);
        }
    }

    public void setOutput(SimStepOutput output) {
        this.output = output;
    }
    public Vector centerOfGravity(Particle[] particles) {
        var sum = new Vector(0, 0, 0);
        for (var p : particles) {
            sum = sum.add(p.position);
        }
        return sum.scale(1.0 / particles.length);
    }
    public Particle[] runSim() throws IOException {
        Particle[] particles = getCopyForSim();
        for (int s = 0; s < steps; s++) {
            output.writeStep(s, particles);
            runSimStep(particles, particles);
        }

        return particles;
    }

    public Particle[] runSimWithSubset(int subsetSize) throws IOException {
        Particle[] particles = getCopyForSim();
        for (int s = 0; s < steps; s++) {
            output.writeStep(s, particles);
            runSimStep(particles, randomSubset(particles, subsetSize));
        }

        return particles;
    }

    private Particle[] getCopyForSim() {
        Particle[] particles = new Particle[initialParticles.length];
        for (int i = 0; i < initialParticles.length; i++) {
            particles[i] = new Particle(initialParticles[i]);
        }
        return particles;
    }

    private void runSimStep(Particle[] particles, Particle[] subset) {
        var forces = new Vector[particles.length];
        for (int i = 0; i < particles.length; i++) {
            var current = particles[i];
            var force = new Vector(0, 0, 0);
            for (Particle other : subset) {
                if (current != other) {
                    Vector f = other.position.subtract(current.position);
                    double distance = f.getMagnitude();
                    Vector forceBetweenParticles = f.interactionForce(distance);
                    force = force.add(forceBetweenParticles);
                }
            }
            forces[i] = force;
        }
        // step 2: calculate new velocities and positions
        for (int i = 0; i < particles.length; i++) {
            var f = forces[i];
            var p = particles[i];
            p.velocity = p.velocity.add(f.scale(dt));
            p.position = p.position.add(p.velocity.scale(dt));
        }
    }

    private Particle[] randomSubset(Particle[] particles, int k) {
        Random random = new Random();
        List<Particle> particleList = new ArrayList();
        Collections.addAll(particleList, particles);
        Collections.shuffle(particleList,random);
        return particleList.subList(0, k).toArray(new Particle[0]);
    }

}
