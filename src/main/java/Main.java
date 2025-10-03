import java.io.IOException;
import java.nio.file.Path;

class Main {

    static final int N = 500;
    static final int k = 500;
    static final int steps = 1000;
    static int runTime = 1000;

    public static void main(String[] args) throws IOException {
        try (var outputAll = new SimulationToCsvWriter(Path.of("simulation.csv"))) {
            ParticleSystem initial = ParticleSystem.createRandomPositionsByDensity(N, 0.001);
            initial.putIDs();
            Simulation sim = new Simulation(steps, runTime);
            sim.setForceCalculationMethod(ForceCalculationMethod.NAIVE_IMMUTABLE);
            var forces  = sim.calculateForces(initial.getParticles(),initial.getParticles());

        }
    }
}