import java.io.IOException;
import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

class Simulation {

    int numParticles;

    double g = 6.67;
    int steps;
    double dt;
    int numSelected;
    Particle[] particles;
    Particle[] subsetParticles;

    public Simulation(int numP, int numS, int s, double deltaT) {
        numParticles = numP;
        numSelected = numS;
        steps = s;
        dt = deltaT;
    }


    public void initRandParticles() {
        particles = new Particle[numParticles];
        Random r = new Random();
        for (int i = 0; i < numParticles; i++) {
            Vector vel = new Vector(0, 0, 0);
            Vector pos = new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble());
            particles[i] = new Particle(pos, vel);
        }
    }

    public void runSim() throws IOException {
        for (int s = 0; s < steps; s++) {
            csvWriter c = new csvWriter("pythonVisuals/particles.csv") ;
            c.writeParticlesToCsv(particles);
            // step 1: calculate new velocities
            for (Particle current : particles) {
                for (Particle others : particles) {
                    var force = new Vector(0, 0, 0);
                    if (current != others) {
                        Vector f = others.position.subtract(current.position);
                        f = f.interactionForce(f.getMagnitude());
                        force = force.add(f);
                    }
                      current.velocity = current.velocity.add(force.scale(dt / (numParticles - 1)));
                }
            }
                // step 2: calculate new positions
                for (Particle p : particles) {
                     p.position = p.position.add(p.velocity.scale(dt));
                }
            }
        }

        public Particle[] randomSubset () {
            List<Particle> particleList = new ArrayList();
            Collections.addAll(particleList, particles);
            Collections.shuffle(particleList);
            Particle[] subsetParticles = new Particle[numSelected];
            for (int i = 0; i < numSelected; i++) {
                subsetParticles[i] = particleList.get(i);
            }

            return subsetParticles;
        }

        public void runSimSubsetofN () {

            for (int s = 0; s < steps; s++) {
                subsetParticles = this.randomSubset();

                // step 1: calculate new velocities
                for (Particle current : particles) {
                    var force = new Vector(0, 0, 0);
                    for (Particle others : this.subsetParticles) {
                        if (current != others) {
                            Vector f = others.position.subtract(current.position);
                            f = f.getUnitVec();
                            f = f.interactionForce(f.getMagnitude());
                            force = force.add(f);
                        }
                    }
                    current.velocity.add((force.scale(1.0 / (subsetParticles.length - 1))).scale(dt));
                }

                // step 2: calculate new posistions
                for (Particle p : particles) {
                    p.position.add(p.velocity.scale(dt));
                }
            }
        }
    }
