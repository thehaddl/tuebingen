import java.io.IOException;
class Main {
    public static void main(String[] args) throws IOException {
        Simulation s = new Simulation(100, 50, 100, 2);
        s.initRandParticles();
        s.runSim();
        System.out.println(s.particles.length);
    }
}


