import java.io.IOException;
import java.util.*;

class Simulation {

    private final Particle[] initialParticles;
    private final int steps;
    private final double dt;

    public Simulation(int numP, int s, double deltaT) {
        steps = s;
        dt = deltaT;
        initialParticles = new Particle[numP];
        Random r = new Random();
        for (int i = 0; i < numP; i++) {
            Vector vel = new Vector(0, 0, 0);
            Vector pos = new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble());
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
        CsvWriter c = new CsvWriter("pythonVisuals/particles.csv");
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

    private void runSimStep(Particle[] particles, Particle[] forcesSubset) {
        // step 1: calculate new velocities
        for (Particle current : particles) {
            var force = new Vector(0, 0, 0);
            for (Particle others : forcesSubset) {
                if (current != others) {
                    Vector f = others.position.subtract(current.position);
                    double distance = f.getMagnitude();
                    Vector forceBetweenParticles = f.interactionForce(distance);
                    force = force.add(forceBetweenParticles);
                }
            }
            current.velocity = current.velocity.add(force.scale(dt));
        }
        // step 2: calculate new positions
        for (Particle a : particles) {
            a.position = a.position.add(a.velocity.scale(dt));
        }
    }

    private Particle[] randomSubset(Particle[] particles, int k) {
        List<Particle> particleList = new ArrayList();
        Collections.addAll(particleList, particles);
        Collections.shuffle(particleList);
        return particleList.subList(0, k).toArray(new Particle[k]);
    }

}
