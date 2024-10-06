import java.io.FileWriter;
import java.io.IOException;
class Main {
    static Particle[] particles1;
    static Particle[] particles2;
    static Particle[] subsetParticles;
    static Particle[] initialParticles;
    static double[] deviation;
    static int nP = 10;
    static int nPs = 4;
    static int steps = 100;
    static double dt = 0.001;

    public static void main(String[] args) throws IOException {
        FileWriter fw = null;
        FileWriter fw2 = null;
        Deviator d = new Deviator(100);
        try {
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
        }

        Simulation s = new Simulation(nP, nPs, steps, dt);
        initialParticles = s.initRandParticles();
        subsetParticles = s.randomSubset(initialParticles);
        particles2 = s.runSimSubsetOfN(initialParticles, subsetParticles);
        particles1 = s.runSim(initialParticles);

        deviation = d.calcDeviation(particles1, particles2);
        for (int i = 0; i < particles2.length; i++) {
           //System.out.println(particles2[i].toCsvString());
        }
    }
}