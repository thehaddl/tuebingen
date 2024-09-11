import java.sql.SQLOutput;
import java.util.Arrays;
class Main {
    public static void main(String[] args) {
        Simulation s = new Simulation(100, 50, 100, 1);
        s.initRandParticles();
        s.runSim();
        for (Particle p : s.particles)
            System.out.println(Arrays.toString(p.position.components));
    }
}


