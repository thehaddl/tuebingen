import java.sql.SQLOutput;

public class Deviator {
    double[] deviationPos;
    double[][] deviationPosPerTime;

    Deviator(int numParticles) {
        this.deviationPos = new double[numParticles];
    }

    public double[] calcDeviation(Particle[] p1, Particle[] p2) {
        for (int i = 0; i < p1.length; i++) {
            Particle a = p1[i];
            Particle b = p2[i];
            deviationPos[i] = a.position.subtract(b.position).getMagnitude();
        }
        return deviationPos;
    }

    public double averageDev(double[] deviation) {
        double dev = 0;
        for (int i = 0; i < deviation.length; i++) {
            dev += deviation[i];
        }
        return dev / (deviation.length);
    }
}
