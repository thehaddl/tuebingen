import java.io.FileWriter;
import java.io.IOException;
class Main {
    static final int nP = 10;
    static final int nPs = 10;
    static final int steps = 100;
    static final double dt = 0.001;

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

        Deviator d = new Deviator(nP);
        Simulation s = new Simulation(nP, steps, dt);
        Particle[] particles2 = s.runSimWithSubset(nPs);
        Particle[] particles1 = s.runSim();

        double[] deviation = d.calcDeviation(particles1, particles2);
        for (int i = 0; i < particles2.length; i++) {
           //System.out.println(particles2[i].toCsvString());
            System.out.println(deviation[i]);
        }
    }
}