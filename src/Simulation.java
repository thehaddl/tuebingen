import java.util.Random;
import java.util.Arrays; 
class Simulation {
    private int numParticles;
    private int numSteps;
    private int numSelected;
    private double dt;
    private Particle[] initialParticles;
    private Particle[] particlesAllInteractions;
    private Particle[] particlesRandomInteractions;
    private double totalPositionDeviation;
    private double totalVelocityDeviation;
    private double dsAllSim;
    private double dvAllSim;
    private double dsRandomSim;
    private double dvRandomSim;
    private double averagePositionDifferenceAll = dsAllSim / (numParticles * 3);
    private double averageVelocityDifferenceRandom = dvRandomSim / (numParticles * 3);
    private double averagePositionDifferenceRandom = dsRandomSim / (numParticles * 3);
    private double averageVelocityDifferenceAll = dvAllSim / (numParticles * 3);
    private double averagePositionDeviation = totalPositionDeviation / (numParticles * 3);
    private double averageVelocityDeviation = totalVelocityDeviation / (numParticles * 3);
    Simulation(int numParticles, int numSteps, int numSelected, double dt) {
        this.numParticles = numParticles;
        this.numSteps = numSteps;
        this.numSelected = numSelected;
        this.dt = dt;
        this.initialParticles = new Particle[numParticles];
        this.particlesAllInteractions = new Particle[numParticles];
        this.particlesRandomInteractions = new Particle[numParticles];
        initializeParticles();
    }

    private void initializeParticles() {
        Random r = new Random();
        for (int i = 0; i < numParticles; i++) {
            Vector position = new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble());
            Vector velocity = new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble());
            initialParticles[i] = new Particle(position, velocity);
            particlesAllInteractions[i] = new Particle(position, velocity);
            particlesRandomInteractions[i] = new Particle(position, velocity);
        }
    }

    void runSimulationAllInteractions() {
        for (int step = 0; step < numSteps; step++) {
            updatePositions(particlesAllInteractions);
            updateForcesAndVelocities(particlesAllInteractions, true);
        }
    }

    void runSimulationRandomInteractions() {
        Random r = new Random();
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

            updatePositions(particlesRandomInteractions);
            updateForcesAndVelocities(particlesRandomInteractions, false, selectedParticles);
        }
    }

    private void updatePositions(Particle[] particles) {
        for (Particle particle : particles) {
            particle.position = particle.position.add(particle.velocity.scale(dt));
        }
    }

    private void updateForcesAndVelocities(Particle[] particles, boolean allInteractions) {
        Vector[] forces = new Vector[numParticles];
        for (int i = 0; i < numParticles; i++) {
            forces[i] = new Vector(0, 0, 0);
        }

        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < numParticles; j++) {
                if (i != j) {
                    Vector force = particles[i].calculateInteractionForce(particles[j].position);
                    forces[i] = forces[i].add(force);
                }
            }
        }

        for (int i = 0; i < numParticles; i++) {
            particles[i].velocity = particles[i].velocity.add(forces[i].scale(dt / (allInteractions ? numParticles : numSelected)));
        }
    }

    private void updateForcesAndVelocities(Particle[] particles, boolean allInteractions, boolean[] selectedParticles) {
        Vector[] forces = new Vector[numParticles];
        for (int i = 0; i < numParticles; i++) {
            forces[i] = new Vector(0, 0, 0);
        }

        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < numParticles; j++) {
                if (i != j && selectedParticles[j]) {
                    Vector force = particles[i].calculateInteractionForce(particles[j].position);
                    forces[i] = forces[i].add(force);
                }
            }
        }

        for (int i = 0; i < numParticles; i++) {
            particles[i].velocity = particles[i].velocity.add(forces[i].scale(dt / (allInteractions ? numParticles : numSelected)));
        }
    }

    void calculateDeviation() {
        dsAllSim = 0;
        dvAllSim = 0;
        dsRandomSim = 0;
        dvRandomSim = 0;
        totalPositionDeviation = 0.0;
        totalVelocityDeviation = 0.0;

        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < 3; j++) {
                dsAllSim += Math.abs(particlesAllInteractions[i].position.components[j] - initialParticles[i].position.components[j]);
                dvAllSim += Math.abs(particlesAllInteractions[i].velocity.components[j] - initialParticles[i].velocity.components[j]);
                dsRandomSim += Math.abs(particlesRandomInteractions[i].position.components[j] - initialParticles[i].position.components[j]);
                dvRandomSim += Math.abs(particlesRandomInteractions[i].velocity.components[j] - initialParticles[i].velocity.components[j]);
                totalPositionDeviation += Math.abs(particlesAllInteractions[i].position.components[j] - particlesRandomInteractions[i].position.components[j]);
                totalVelocityDeviation += Math.abs(particlesAllInteractions[i].velocity.components[j] - particlesRandomInteractions[i].velocity.components[j]);
            }
        }

        System.out.println("the difference in positon for simulating under account of all forces was: " + averagePositionDifferenceAll);
        System.out.println("the difference in velocity for simulating under account of all forces was: " + averageVelocityDifferenceAll);
        System.out.println("the difference in position for simulating under account of some of the forces was: " + averagePositionDifferenceRandom);
        System.out.println("the difference in velocity for simulating under account of some of the forces was: " + averageVelocityDifferenceRandom);

        System.out.println("Average Position Deviation: " + averagePositionDeviation);
        System.out.println("Average Velocity Deviation: " + averageVelocityDeviation);
    }

    void analyzeDeviation() {
        runSimulationAllInteractions();
        for (int n = 50; n < (numParticles - 50); n += 50) {
            numSelected = n;
            System.out.println("Analyzing deviation for numSelected = " + numSelected);
            runSimulationRandomInteractions();
            calculateDeviation();
        }
    }
}
