<<<<<<< HEAD
class Main{
    public static void main(String[] args) {
        Simulation s = new Simulation(100, 50, 100,1);
        s.runSim();
        for(Particle p : s.particles)
            System.out.println(Array.deepToString(p));
    }
=======
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

  /*  public static void analyzeDeviation(){
        double out;
        Random rand = new Random();


        for(int i = 0; i< numParticles; i++){
            numSelected =i;

            for (int k = 0; k < numParticles; k++) {
                for (int j = 0; j < 3; j++) {
                    initialPositions[k][j] = rand.nextDouble(); // Zufällige Anfangspositionen
                    initialVelocities[k][j] = rand.nextDouble(); // Zufällige Anfangsgeschwindigkeiten
                }
            }

            runSimulationAllInteractions();
            runSimulationRandomInteractions();
            calculateDeviation();
            out = 1/Math.sqrt(i);
            System.out.println(averagePositionDeviation+ " bei k = "+i);
            System.out.println(out);

        }*/
>>>>>>> master
}