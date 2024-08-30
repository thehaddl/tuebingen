import java.util.Random;

class Simulation {
    int numParticles = 10;
    double g = 6.67;
    int steps = 100;
    double dt = 0.1;
    Particle[] particles = new Particle[numParticles];

    Vector interactionForce(Vector pos) {
        Vector force = new Vector(pos.components)
        forceScalar = (1 / this.calcDistance(pos) * this.calcDistance(pos)) * g;
        return (force/pos.getMagnitude()) * forceScalar;
    }

    public initRandParticles() {
        for (int i = 0; i < numParticles; i++) {
            Random r = new Random();
            double[] randPos = new double[3];
            for (int j = 0; j < 3; j++) {
                randPos[j] = r.nextDouble;
            }
            Vector f = new Vector({0,0,0})
            Vector vel = new Vector({1, 1, 1});
            Vector pos = new Vector(randPos);
            particles[i] = new Particle(pos, vel);
        }
    }

    public runSim() {
        for (int s = 0; s < steps; s++) {

            for (Particle p : particles) {
                p.position = p.velocity * dt;
            }
        }
        /*for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < numParticles; j++) {
                if (i != j) {
                    Vector vecitoj = particles[j].position.subtract(particles[i].position)

                    Vector force = this.interactionForce(vecItoJ);
                    forces[i].add(force);

                }
            }
        }*/
        for(Particle current : particles){
            for(Particle others : particles){
                if(current.equals(others) == false){
                    Vector vecItoJ = others.position.subtract(current.position)
                    Vector f = this.interactionForce(vecItoJ);
                    current.force.add(f);
                }
            }
        }
        for (Particle p : particles) {
                p.velocity += (p.force / numParticles) * dt;
        }

    }

}