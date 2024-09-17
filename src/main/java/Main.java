import java.io.IOException;
class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("pythonVisuals/particles.csv");
        f.delete();
        Simulation s = new Simulation(10, 50, 30, 0.05);
        s.initRandParticles();
        s.runSim();
        System.out.println(s.particles.length);
    }
}


