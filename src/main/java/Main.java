import java.io.IOException;
class Main {
    public static void main(String[] args) throws IOException {
        Simulation s = new Simulation(100, 50, 100, 1);
        s.initRandParticles();
        s.runSim();
        csvWriter writer = new csvWriter("data.csv");
        writer.writeParticlesToCsv(s.particles);
    }
}


