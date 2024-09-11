import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
class Simulation {

    int numParticles;
    double numP= numParticles;
    double g = 6.67;
    int steps;
    double dt;
    int numSelected;
    Particle[] particles = new Particle[numParticles];
    Particle[] subsetParticles = new Particle[numSelected];

    public Simulation(int numP,int numS,int s,double deltaT){
        numParticles = numP;
        numSelected = numS;
        steps = s;
        dt = deltaT;
    }


    public void initRandParticles() {
        for (int i = 0; i < numParticles; i++) {
            Random r = new Random();
            double[] randPos = new double[3];
            for (int j = 0; j < 3; j++) {
                randPos[j] = r.nextDouble();
            }
            double[] d = {0, 0, 0};
            Vector f = new Vector(d);
            double[] unit = {1, 1, 1};
            Vector vel = new Vector(unit);
            Vector pos = new Vector(randPos);
            particles[i] = new Particle(pos, vel, f);
        }
    }

    public void runSim() {
        for (int s = 0; s < steps; s++) {

            for (Particle p : particles) {
                p.position.add(p.velocity.scale(dt));

            }

            for (Particle current : particles) {
                for (Particle others : particles) {
                    if (current.equals(others) == false) {
                        Vector vecItoJ = others.position.subtract(current.position);
                        Vector f = vecItoJ.interactionForce(vecItoJ);
                        current.force.add(f);
                    }
                }
            }
            for (Particle p : particles) {
                p.velocity.add(p.force.scale(1/numP)).scale(dt);
            }

        }
    }

    public Particle[] randomSubset() {
        List<Particle> particleList = new ArrayList();
        Collections.addAll(particleList, particles);
        Collections.shuffle(particleList);
        Particle[] subsetParticles = new Particle[numSelected];
        for (int i = 0; i < numSelected; i++) {
            subsetParticles[i] = particleList.get(i);
        }

        return subsetParticles;
    }

    public void runSimSubsetofN() {
        for (int s = 0; s < steps; s++) {

            for (Particle p : this.subsetParticles) {
                p.position.add(p.velocity.scale(dt));
            }

            for (Particle current : particles) {
                for (Particle others : this.subsetParticles) {
                    if (current.equals(others) == false) {
                        Vector vecItoJ = others.position.subtract(current.position);
                        Vector f = vecItoJ.interactionForce(vecItoJ);
                        current.force.add(f);
                    }
                }
            }
            for (Particle p : particles) {
                p.velocity.add((p.force.scale(1/  numParticles)).scale(dt));
            }

        }
    }
}