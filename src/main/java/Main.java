import java.io.FileWriter;
import java.io.IOException;
class Main {
    static final int nP = 100;
    static final int nPs = 100;
    static final int steps = 1000;
    static final double dt = 0.005;

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
        try (FileWriter f = new FileWriter("deviation.csv", false)) {
            double[] avdevs = new double[nPs];
            f.write("k,d\n");
            Deviator d = new Deviator(nP);
            Simulation s = new Simulation(nP, steps, dt);
            Particle[] particles1 = s.runSim();
            for (int i = 1; i <= nPs; i++) {
                Particle[] particles2 = s.runSimWithSubset(i);
                double[] deviation = d.calcDeviation(particles1, particles2);
                double avdev = d.averageDev(deviation);
                avdevs[i-1] = avdev;
                System.out.println("deviation for k = " + i +"\n");
                System.out.println(avdev);
                f.append(i+ "," + avdev + "\n");
                f.flush();
                System.out.println(particles2[i-1].toCsvString());
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