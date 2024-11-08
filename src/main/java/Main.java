import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

class Main {
    static final int nP = 300;
    static final int nPs = 300;
    static final int steps = 1000;

//    public static void main(String[] args) throws IOException {
//        try (FileWriter f = new FileWriter("deviation.csv", false);
//             var outputAll = new CsvWriter(Path.of("allparticles.csv"));
//             var outputSubset = new CsvWriter(Path.of("subparticles.csv"))) {
//
//            f.write("k,d\n");
//            var initial = ParticleSystem.createRandomPositions(nP, 10);
//            Simulation s = new Simulation(steps, 100);
//            s.setOutput(outputAll);
//            var resultAllParticles = s.runSim(initial);
//            s.setOutput(outputSubset);
//
//            for (int i = 1; i <= nPs; i++) {
//                var resultSubParticles = s.runSimWithSubset(initial, i);
//                double averageDeviation = resultSubParticles.calcAverageDeviation(resultAllParticles);
//                System.out.println("deviation for k = " + i + "\n");
//                System.out.println(averageDeviation);
//                f.append(i + "," + averageDeviation + "\n");
//                f.flush();
//            }
//
//        }
//    }
public static void main(String[] args) {
    var initial = ParticleSystem.createRandomPositions(nP, 10);
    Simulation s = new Simulation(steps, 100);
    var resultAllParticles = s.runSim(initial);
    var resulSubParticles = s.runSimWithSubset(initial,50);
    for(int i = 0; i < nPs;i++){
        System.out.println("All particles: " +resultAllParticles.getParticles()[i].toString() + " Subset Particles : " + resulSubParticles.getParticles()[i].toString());
    }
}

}