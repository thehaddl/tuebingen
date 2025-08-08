import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

class Main {

    static final int nP = 500;
    static final int nPs = 100;
    static final int steps = 10000;
    static final double dimension = 70 ;

    public static void main(String[] args) throws IOException {
        try (FileWriter f = new FileWriter("deviation.csv", false);
             var outputAll = new CsvWriter(Path.of("allparticles.csv"));
             var outputSubset = new CsvWriter(Path.of("subparticles.csv"))) {

            f.write("k,d\n");
//            Particle[] p = new Particle[2];
//            p[0]= new Particle(new Vector(1,1,1),new Vector(0,0,0),1,0);
//            p[1]= new Particle(new Vector(-1,-1,-1),new Vector(0,0,0),1,0);
//            ParticleSystem initial = ParticleSystem.createFrom(p);
            ParticleSystem initial = ParticleSystem.createRandomPositions(nP,dimension);

            initial.putIDs();
            initial.putRandomCharges();
            //initial.putRandomVelocities(dimension/100);

            Simulation s = new Simulation(steps, 1000);

            s.setOutput(outputAll);
            var resultAllParticles = s.runSim(initial);
            //s.setOutput(outputSubset);
            //var resultSubParticles = s.runSimWithSubset(initial,nPs);

//            for (int i = 1; i <= nPs; i++) {
//                var resultSubParticles = s.runSimWithSubset(initial, i);
//                double averageDeviation = resultSubParticles.calcAverageDeviation(resultAllParticles);
//                System.out.println("deviation for k = " + (double) i/nP + "\n");
//                System.out.println(averageDeviation);
//                f.append((double) i/nP + "," + averageDeviation+ "\n");
//                f.flush();
//            }

        }
    }


}