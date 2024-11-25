import java.util.*;
import java.util.List;

class Simulation {
    private final double runTime;
    private final int steps;
    private final double dt;
    long runTimeAll;
    long runTimeSub;

    private SimStepOutput output = SimStepOutput.NOP;


    public Simulation(int steps, double runTime) {
        this.steps = steps;
        this.runTime = runTime;
        dt = runTime/steps;

    }

    public void setOutput(SimStepOutput output) {
        this.output = output;
    }

    //runs simulation with all particles (n^2 terms)
    public ParticleSystem runSim(ParticleSystem initial){
        long startTime = System.nanoTime();
        Particle[] particles = initial.getParticles();
        for (int s = 0; s < steps; s++) {
            output.writeStep(s, particles);
            runSimStep(particles, particles);
        }
        long stopTime = System.nanoTime();
        runTimeAll = stopTime-startTime;
        return ParticleSystem.createFrom(particles);
    }

    //runs simulation with a given subset (only k terms)
    public ParticleSystem runSimWithSubset(ParticleSystem initial, int subsetSize){
        long startTime = System.nanoTime();
        Particle[] particles = initial.getParticles();
        for (int s = 0; s < steps; s++) {
            output.writeStep(s, particles);
            runSimStep(particles, randomSubset(particles, subsetSize));
        }
        long stopTime = System.nanoTime();
        runTimeSub = stopTime-startTime;
        return ParticleSystem.createFrom(particles);

    }
    // calculates a general Simulation Step (when k = n then subset is just all particles)
    private void runSimStep(Particle[] particles, Particle[] subset) {
        var forces = new Vector[particles.length];
        var subsetSize = 1.0/subset.length;
        //step 1: calculate forces
        for (int i = 0; i < particles.length; i++) {
            var current = particles[i];
            var force = new Vector(0, 0, 0);
            for (Particle other : subset) {
                if (current != other) {
                    force = force.add(current.getGravitationalForce(other)).scale(subsetSize).add(current.getElectricalForce(other).scale(subsetSize));
                }
            }
            forces[i] = force;
        }
        // step 2: calculate new velocities and positions
        for (int i = 0; i < particles.length; i++) {
            var f = forces[i];
            var p = particles[i];
            p.velocity = p.velocity.add(f.scale(dt));
            p.position = p.position.add(p.velocity.scale(dt));
        }
    }
    //needs to be refactored into ParticleSystem class
    private Particle[] randomSubset(Particle[] particles, int k) {
        Random random = new Random();
        List<Particle> particleList = new ArrayList();
        Collections.addAll(particleList, particles);
        Collections.shuffle(particleList,random);
        return particleList.subList(0, k).toArray(new Particle[0]);
    }

}
