import java.util.Random;

public class Main { 
    public static void main(String[] args) {
        Simulation simulation = new Simulation(1000, 1000, 4, 0.001);
        simulation.runSimulationAllInteractions();
        simulation.analyzeDeviation();
    }
          
}
