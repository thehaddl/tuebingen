import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

class Main {

    static final int N = 5000;
    static final int k = 2500;
    static final int steps = 1000;
    static int runTime = 1000;
    static final double dimension = N/100 ;

    public static void main(String[] args) throws IOException {
    Simulation s= new Simulation(steps,runTime);
    ParticleSystem initial = ParticleSystem.createRandomPositionsByDensity(N,0.1);
    s.setForceCalculationMethod(ForceCalculationMethod.PARALLEL_MUTABLE);
    s.setParticleSampler(new RandomParticleSampler(k));
    ParticleSystem sample = s.runSim(initial);
    s.setParticleSampler(new AllParticleSampler());
    ParticleSystem total = s.runSim(initial);
    SimulationComparer sc = sample.compare(total);
    sc.toString();
    }


}