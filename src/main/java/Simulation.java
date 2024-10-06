import java.io.IOException;
import java.util.*;

class Simulation {

    int numParticles;

    double g = 6.67;
    int steps;
    double dt;
    int numSelected;

    public Simulation(int numP, int numS, int s, double deltaT) {
        numParticles = numP;
        numSelected = numS;
        steps = s;
        dt = deltaT;
    }


    public Particle[] initRandParticles() {
        Particle[] particles = new Particle[numParticles];
        Random r = new Random();
        for (int i = 0; i < numParticles; i++) {
            Vector vel = new Vector(0, 0, 0);
            Vector pos = new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble());
            particles[i] = new Particle(pos, vel);
        }
        return particles;
    }

    public Particle[] randomSubset(Particle[] particles) {
        List<Particle> particleList = new ArrayList();
        Collections.addAll(particleList, particles);
        Collections.shuffle(particleList);
        Particle[] subsetParticles = new Particle[numSelected];
        for (int i = 0; i < numSelected; i++) {
            subsetParticles[i] = particleList.get(i);
        }

        return subsetParticles;
    }

    public Particle[] randomSubset(Particle[] particles, int k) {
        List<Particle> particleList = new ArrayList();
        Collections.addAll(particleList, particles);
        Collections.shuffle(particleList);
        Particle[] subsetParticles = new Particle[k];
        for (int i = 0; i < k; i++) {
            subsetParticles[i] = particleList.get(i);
        }

        return subsetParticles;
    }
    public Particle[] runSim(Particle[] p) throws IOException {
        Particle[] particles = new Particle[numParticles];
        for(int i = 0; i < p.length; i++){
            particles[i] = new Particle(p[i]);
        }
        csvWriter c = new csvWriter("pythonVisuals/particles.csv");
        for (int s = 0; s < steps; s++) {
            c.writeParticlesToCsv(particles);
            // step 1: calculate new velocities
            for (Particle current : particles) {
                var force = new Vector(0, 0, 0);
                for (Particle others : particles) {
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

        return particles;
    }

    public Particle[] runSimSubsetOfN(Particle[] p, Particle[] subset) throws IOException {
        csvWriter c = new csvWriter("pythonVisuals/particles2.csv");
        //Deepcopy inputs
        Particle[] particles = new Particle[numParticles];
        Particle[] subsetParticles = new Particle[numSelected];
        for(int i = 0; i < p.length; i++){
            particles[i] = new Particle(p[i]);
        }
        for(int i = 0; i < subset.length; i++){
            subsetParticles[i] = new Particle(subset[i]);
        }

        for (int s = 0; s < steps; s++) {
            c.writeParticlesToCsv(particles);
            // step 1: calculate new velocities
            for (Particle current : particles) {
                var force = new Vector(0, 0, 0);
                for (Particle others : subsetParticles) {
                    if (current != others) {
                        Vector f = others.position.subtract(current.position);
                        double distance = f.getMagnitude();
                        Vector forceBetweenParticles = f.interactionForce(distance);
                        force = force.add(forceBetweenParticles);
                    }
                    else{
                        System.out.println("vielleicht ja hier????");
                    }
                }
                System.out.println(current.velocity.toCsvString());
                current.velocity = current.velocity.add(force.scale(dt));
            }

            // step 2: calculate new positions
            for (Particle a : particles) {
                a.position = a.position.add(a.velocity.scale(dt));
            }

        }

        return particles;
    }
}
