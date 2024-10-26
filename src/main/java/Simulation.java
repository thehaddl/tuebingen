import java.io.IOException;
import java.util.*;

class Simulation {

    public static final double SCALAR = 1000;
    private final Particle[] initialParticles;
    private final int steps;
    private final double dt;

    public Simulation(int numP, int s, double deltaT) {
        steps = s;
        dt = deltaT;
        initialParticles = new Particle[numP];
        Random r = new Random();
        for (int i = 0; i < numP; i++) {
            Vector vel = new Vector(0,0,0);
            Vector pos = new Vector(r.nextDouble()* SCALAR, r.nextDouble()* SCALAR, r.nextDouble()* SCALAR);
            initialParticles[i] = new Particle(pos, vel);
        }
    }

    public Particle[] runSim() throws IOException {
        Particle[] particles = getCopyForSim();
        CsvWriter c = new CsvWriter("pythonVisuals/particles.csv");
        for (int s = 0; s < steps; s++) {
            c.writeParticlesToCsv(particles);
            runSimStep(particles, particles);
        }

        return particles;
    }

    public Particle[] runSimWithSubset(int subsetSize) throws IOException {
        Particle[] particles = getCopyForSim();
        CsvWriter c = new CsvWriter("pythonVisuals/particles2.csv");
        for (int s = 0; s < steps; s++) {
            c.writeParticlesToCsv(particles);
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
