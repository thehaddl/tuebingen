public class Deviator
{
    double[] deviationPos;
    double[][] deviationPosPerTime;
    Deviator(int numParticles){
        this.deviationPos = new double[numParticles];
    }
    public double[] calcDeviation(Particle[] p1, Particle[]p2){
        for(int i = 0;i < p1.length;i++){
            Particle a = p1[i];
            Particle b = p2[i];
            deviationPos[i] = Math.abs(a.position.subtract(b.position).getMagnitude());
        }
        return deviationPos;
    }
    public double[][] calcDeviationPerTime(Particle[] p1, Particle[]p2, int k){
        for(int i = 0;i < p1.length;i++){
            Particle a = p1[i];
            Particle b = p2[i];
            deviationPosPerTime[i][k] = Math.abs(a.position.subtract(b.position).getMagnitude());
        }
        return deviationPosPerTime;
    }
}
