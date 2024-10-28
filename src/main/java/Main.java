import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

class Main {
    static final int nP = 300;
    static final int nPs = 300;
    static final int steps = 1000;
    public static void main(String[] args) throws IOException {
        try (FileWriter f = new FileWriter("deviation.csv", false);
             var outputAll = new CsvWriter(Path.of("allparticles.csv"));
             var outputSubset = new CsvWriter(Path.of("subparticles.csv"))) {

            f.write("k,d\n");
            Deviator d = new Deviator(nP);
            var initial = ParticleSystem.createRandomPositions(nP, 1000);
            Simulation s = new Simulation(steps, 100);
            s.setOutput(outputAll);
            var resultAllParticles = s.runSim(initial);
            s.setOutput(outputSubset);

            for (int i = 1; i <= nPs; i++) {
                var resultSubParticles = s.runSimWithSubset(initial, i);
                double[] deviation = d.calcDeviation(resultAllParticles.getParticles(), resultSubParticles.getParticles());
                double avdev = d.averageDev(deviation);
                System.out.println("deviation for k = " + i + "\n");
                System.out.println(avdev);
                f.append(i + "," + avdev + "\n");
                f.flush();
            }
        }
    }
}