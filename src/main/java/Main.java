import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

class Main {

    static final int N = 500;
    static final int k = 500;
    static final int steps = 1000;
    static int runTime = 1000;
    static final double dimension = N/100 ;

    public static void main(String[] args) throws IOException {
    var outputAll = new CsvWriter(Path.of("simulation.csv"));
    var outputSample = new CsvWriter(Path.of("sampleSimulation.csv"));
    Simulation sampleSim= new Simulation(steps,runTime);
    ParticleSystem initial = ParticleSystem.createRandomPositionsByDensity(N,0.01);
    initial.putIDs();
    sampleSim.setForceCalculationMethod(ForceCalculationMethod.NAIVE_IMMUTABLE);
    sampleSim.setParticleSampler(new RandomParticleSampler(k));
    sampleSim.setOutput(outputSample);
    ParticleSystem sample = sampleSim.runSim(initial);
    Simulation sim= new Simulation(steps,runTime);
    sim.setForceCalculationMethod(ForceCalculationMethod.NAIVE_IMMUTABLE);
    sim.setParticleSampler(new AllParticleSampler());
    sim.setOutput(outputAll);
    ParticleSystem total = sim.runSim(initial);
    SimulationComparer sc = sample.compare(total);
    System.out.println(sc.toString());
    }


}