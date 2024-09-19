import java.io.File;
import java.io.IOException;
class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("pythonVisuals/particles.csv");
        File g = new File("pythonVisuals/particles2.csv");
        f.delete();
        g.delete();
        Simulation s = new Simulation(20, 2, 1000, 0.01);
        s.initRandParticles();
        s.initialParticles = new Particle[s.particles.length];
        for (int i = 0; i < s.particles.length; i++) {
            s.initialParticles[i] = new Particle(s.particles[i].position, s.particles[i].velocity); // Create a new instance
        }
        System.out.println(s.initialParticles[1].velocity.getMagnitude());

        s.runSim();
        System.out.println(s.initialParticles[1].velocity.getMagnitude());
        s.particles = new Particle[s.initialParticles.length];
        for (int i = 0; i < s.particles.length; i++) {
            s.particles[i] = new Particle(s.initialParticles[i].position, s.initialParticles[i].velocity); // Create a new instance
        }
        System.out.println(s.initialParticles[1].velocity.getMagnitude());
        s.runSimSubsetOfN();

    }
}


