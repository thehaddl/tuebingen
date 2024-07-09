import com.sun.source.tree.IdentifierTree;

import java.text.CollationElementIterator;
import java.util.*;

public class ParticleSimulation {

    static int numParticles = 1000;
    static double dsAllSim=0;
    /*static List<Integer> randomIndices= new ArrayList<>();

    static{for(int i = 0; i < numParticles; i++){
        randomIndices.add(i);
    }}*/

    static double dvAllSim=0;
    static double dsRandomSim=0;
    static double dvRandomSim=0;
    static int numSteps= 1000;
    static int numSelected = 4;
    static double totalPositionDeviation = 0.0;
    static double totalVelocityDeviation = 0.0;
    static double averagePositionDeviation;
    static double averageVelocityDeviation;
    static double averagePositionDifferenceAll;
    static double averageVelocityDifferenceRandom;
    static double averagePositionDifferenceRandom;
    static double averageVelocityDifferenceAll;
    static double[][] initialPositions = new double[numParticles][3];
    static double[][] initialVelocities = new double[numParticles][3];
    static double[][] positionsAllInteractions = new double[numParticles][3]; // 3D-Positionen für Simulation mit allen Interaktionen
    static double[][] positionsRandomInteractions = new double[numParticles][3]; // 3D-Positionen für Simulation mit zufälligen Interaktionen
    static double[][] velocitiesAllInteractions = new double[numParticles][3]; // 3D-Geschwindigkeiten für Simulation mit allen Interaktionen
    static double[][] velocitiesRandomInteractions = new double[numParticles][3]; // 3D-Geschwindigkeiten für Simulation mit zufälligen Interaktionen

    static double dt = 0.001;

    static double[] interactionForce(double[] x) {
        double[] force = new double[3];
        double lengthSquared = x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
        for (int i = 0; i < 3; i++) {
            force[i] = x[i] * lengthSquared;
        }
        return force;
    }

    static void runSimulationAllInteractions() {

        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < 3; j++) {
                positionsAllInteractions[i][j] = initialPositions[i][j];
                velocitiesAllInteractions[i][j] = initialVelocities[i][j];
            }
        }


        for (int step = 0; step < numSteps; step++) {
            double[][] forces = new double[numParticles][3];
            for(int i = 0; i< numParticles;i++){
                for(int j = 0; j<3;j++){
                    forces[i][j] = 0;
                }
            }
            // neue positionen für jedes teilchen i in jeder dimension j
            for (int i = 0; i < numParticles; i++) {
                for (int j = 0; j < 3; j++) {
                    positionsAllInteractions[i][j] += velocitiesAllInteractions[i][j] * dt;
                }
            }



            for (int i = 0; i < numParticles; i++) {
                for (int j = 0; j < numParticles; j++) {
                    if (i != j) {
                        double[] vecjveci = new double[3];
                        for (int k = 0; k < 3; k++) {
                            vecjveci[k] = positionsAllInteractions[j][k] - positionsAllInteractions[i][k];
                        }
                        double[] finalforce = interactionForce(vecjveci);
                        for (int k = 0; k < 3; k++) {
                            forces[i][k] += finalforce[k];
                        }
                    }
                }
            }

            for (int i = 0; i < numParticles; i++) {
                for (int j = 0; j < 3; j++) {
                    velocitiesAllInteractions[i][j] += (forces[i][j] / numParticles) * dt;
                }
            }
        }


    }

    // Simulationsschleife mit Wechselwirkungen zwischen k zufälligen Teilchen
    static void runSimulationRandomInteractions() {
        Random r = new Random();
        // Initialisierung der Positionen und Geschwindigkeiten
        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < 3; j++) {
                positionsRandomInteractions[i][j] = initialPositions[i][j]; // Set initial positions
                velocitiesRandomInteractions[i][j] = initialVelocities[i][j]; // Set initial velocities
            }
        }

        // Simulationsschleife
        //Collections.shuffle(randomIndices);

        for (int step = 0; step < numSteps; step++) {
            boolean[] selectedParticles = new boolean[numParticles];

            int selectedCount = 0;

            while (selectedCount < numSelected) {
                int particle = r.nextInt(numParticles);
                if (!selectedParticles[particle]) {
                    selectedParticles[particle] = true;
                    selectedCount++;
                }
            }

            for (int i = 0; i < numParticles; i++) {
                for (int j = 0; j < 3; j++) {
                    positionsRandomInteractions[i][j] += velocitiesRandomInteractions[i][j] * dt;
                }
            }

            double[][] forces = new double[numParticles][3];
            for(int i = 0; i< numParticles;i++){
                for(int j = 0; j<3;j++){
                    forces[i][j] = 0;
                }
            }
            // Berechnung der neuen Positionen

            // berechnung der kraft vektoren für jedes partikel i

            for (int i = 0; i < numParticles; i++) {
                for (int j = 0; j < numParticles; j++) {


                        if (i != j && selectedParticles[j]) {
                            double[] vecjveci = new double[3];
                            for (int k = 0; k < 3; k++) {
                                vecjveci[k] = positionsRandomInteractions[j][k] - positionsRandomInteractions[i][k];
                            }
                            double[] finalforce = interactionForce(vecjveci);
                            for (int k = 0; k < 3; k++) {
                                forces[i][k] += finalforce[k];
                            }
                        }
                    }

            }
            // Aktualisierung der Geschwindigkeiten
            for (int i = 0; i < numParticles; i++) {
                for (int j = 0; j < 3; j++) {
                    velocitiesRandomInteractions[i][j] += (forces[i][j] / numSelected) * dt;
                }
            }
        }

    }

    // Funktion zur Berechnung der Abweichung der Positionen und Geschwindigkeiten
    static void calculateDeviation() {
        dsAllSim = 0; // Correction: Reset cumulative sums for each call
        dvAllSim = 0;
        dsRandomSim = 0;
        dvRandomSim = 0;
        totalPositionDeviation = 0.0;
        totalVelocityDeviation = 0.0;
        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < 3; j++) {
                dsAllSim += Math.abs(positionsAllInteractions[i][j]-initialPositions[i][j]);
                dvAllSim += Math.abs(velocitiesAllInteractions[i][j]-initialVelocities[i][j]);
                dsRandomSim += Math.abs(positionsRandomInteractions[i][j]-initialPositions[i][j]);
                dvRandomSim += Math.abs(velocitiesRandomInteractions[i][j]-initialVelocities[i][j]);
                totalPositionDeviation += Math.abs(positionsAllInteractions[i][j] - positionsRandomInteractions[i][j]);
                totalVelocityDeviation += Math.abs(velocitiesAllInteractions[i][j] - velocitiesRandomInteractions[i][j]);
                if (Double.isNaN(totalPositionDeviation) || Double.isNaN(totalVelocityDeviation)) {
                    System.err.println("NaN detected at particle " + i + ", dimension " + j);
                    return;
                }
            }

        }
        averagePositionDifferenceAll = dsAllSim/(numParticles*3);
        averageVelocityDifferenceRandom = dvRandomSim/(numParticles*3);
        averagePositionDifferenceRandom = dsRandomSim/(numParticles*3);
        averageVelocityDifferenceAll = dvAllSim/(numParticles*3);
        averagePositionDeviation = totalPositionDeviation/(numParticles*3);
        averageVelocityDeviation = totalVelocityDeviation/(numParticles*3);
        System.out.println("the difference in positon for simulating under account of all forces was: "+ averagePositionDifferenceAll);
        System.out.println("the difference in velocity for simulating under account of all forces was: "+ averageVelocityDifferenceAll);
        System.out.println("the difference in position for simulating under account of some of the forces was: "+ averagePositionDifferenceRandom);
        System.out.println("the difference in velocity for simulating under account of some of the forces was: "+ averageVelocityDifferenceRandom);

        System.out.println("Average Position Deviation: " +averagePositionDeviation );

        System.out.println("Average Velocity Deviation: " + averageVelocityDeviation);
    }
    // Neue Methode zur Analyse der Abweichung für verschiedene Werte von numSelected
    static void analyzeDeviation() {
        runSimulationAllInteractions();
        for (int n = 50; n < (numParticles-50); n += 50) {
            // Werte von numSelected systematisch durchgehen (1, 2, 4, 8, ..., numParticles)
            numSelected = n;

            System.out.println("Analyzing deviation for numSelected = " + numSelected);

            runSimulationRandomInteractions();
            calculateDeviation();
            double sm = 1/Math.sqrt(n);
            System.out.println( averagePositionDeviation);
        }
    }
    public static void main(String[] args) {
        Random rand = new Random();
        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < 3; j++) {
                initialPositions[i][j] = rand.nextDouble(); // Zufällige Anfangspositionen
                initialVelocities[i][j] = rand.nextDouble(); // Zufällige Anfangsgeschwindigkeiten
            }
        }

        System.out.println("Initial positions: " + Arrays.deepToString(initialPositions));
        System.out.println("Initial velocities: " + Arrays.deepToString(initialVelocities));

        analyzeDeviation();
    }




    public static void main2(String[] args) {
        Random rand = new Random();
        //int numSelectedParticles = rand.nextInt(numParticles - 1) + 1; // Zufällige Anzahl numSelectedParticles (mindestens 1)
        //numSelected = numSelectedParticles;
        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < 3; j++) {
                initialPositions[i][j] = rand.nextDouble() ; // Zufällige Anfangspositionen
                initialVelocities[i][j] = rand.nextDouble(); // Zufällige Anfangsgeschwindigkeiten
            }
        }


        System.out.println(Arrays.deepToString(initialPositions));
        System.out.println(Arrays.deepToString(initialVelocities));
        System.out.println(initialPositions.length);
        System.out.println(initialVelocities.length);
        System.out.println("Simulation with all interactions:");
        long startTime = System.currentTimeMillis();
        runSimulationAllInteractions();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("runtime simulation with all interactionns: " + elapsedTime +" ms");
        System.out.println("Simulation with random interactions:");
        long startTime1 = System.currentTimeMillis();
        runSimulationRandomInteractions();
        long endTime1 = System.currentTimeMillis();
        long elapsedTime1 = endTime1 - startTime1;
        System.out.println("runtime simulation with "+ numSelected +" interactionns: " + elapsedTime1+ " ms");
        System.out.println("runtime deviation:" + (elapsedTime - elapsedTime1) + " ms");
        for(int j= 0; j<numParticles;j++) {
            for (int k = 0; k < 3; k++) {
                dsAllSim += Math.abs(positionsAllInteractions[j][k]-initialPositions[j][k]);
                dvAllSim += Math.abs(velocitiesAllInteractions[j][k]-initialVelocities[j][k]);
                dsRandomSim += Math.abs(positionsRandomInteractions[j][k]-initialPositions[j][k]);
                dvRandomSim += Math.abs(velocitiesRandomInteractions[j][k]-initialVelocities[j][k]);

            }
        }
        System.out.println("delta S when simulating with all particles: "+ dsAllSim);
        System.out.println("delta S when simulating with all particles: "+ dsRandomSim);
        calculateDeviation();



    }
}
