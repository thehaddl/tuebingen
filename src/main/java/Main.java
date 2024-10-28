import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

class Main {
    static final int nP = 300;
    static final int nPs = 300;
    static final int steps = 1000;
    static final double dt = 0.05;

    public static void main(String[] args) throws IOException {
        //FileWriter fw = null;
        //FileWriter fw2 = null;
        /*try {
            fw = new FileWriter("pythonVisuals/particles.csv");
            fw2 = new FileWriter("pythonVisuals/particles2.csv");

            fw.write("x1,x2,x3,y1,y2,y3,\n");
            fw2.write("x1,x2,x3,y1,y2,y3,\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) fw.close();
                if (fw2 != null) fw2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        try (FileWriter f = new FileWriter("deviation.csv", false);
             var outputAll = new CsvWriter(Path.of("allparticles.csv"));
             var outputSubset = new CsvWriter(Path.of("subparticles.csv"))) {
            double[] avdevs = new double[nPs];
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
                avdevs[i - 1] = avdev;
                System.out.println("deviation for k = " + i + "\n");
                System.out.println(avdev);
                f.append(i + "," + avdev + "\n");
                f.flush();

            }


        }
//            Deviator d = new Deviator(nP);
//            Simulation s = new Simulation(nP, steps, dt);
//            Particle[] particles1 = s.runSim();
//            Particle[] particles2 = s.runSimWithSubset(nPs);
//            double[] deviation = d.calcDeviation(particles1, particles2);
//            double avdev = d.averageDev(deviation);
//            System.out.println(avdev);
    }
}