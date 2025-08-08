import java.util.*;
import java.util.List;

class Simulation {
    private final int steps;
    private final double dt;
    private SimStepOutput output = SimStepOutput.NOP;


    public Simulation(int steps, double runTime) {
        this.steps = steps;
        dt = runTime/steps;

    }

    public void setOutput(SimStepOutput output) {
        this.output = output;
    }

    //runs simulation with all particles (n*(n-1) terms)
    public ParticleSystem runSim(ParticleSystem initial){
        List<Particle> particles = initial.getParticles();
        for (int s = 0; s < steps; s++) {
            output.writeStep(s, particles);
            runSimStep(particles, particles);
        }
        return ParticleSystem.createFrom(particles);
    }

    //runs simulation with a given subset (only k * n terms)
    public ParticleSystem runSimWithSubset(ParticleSystem initial, int subsetSize){
        List<Particle> particles = initial.getParticles();
        for (int s = 0; s < steps; s++) {
            output.writeStep(s, particles);
            runSimStep(particles, randomSubset(particles, subsetSize));
        }
        return ParticleSystem.createFrom(particles);

    }
    // calculates a general Simulation Step (when k = n then subset is just all particles)
    private void runSimStep(List<Particle> particles, List<Particle> subset) {
        List<Vector> forces = new ArrayList<>();
        double GRAVITY = 1.0/subset.size();
        //step 1: calculate forces
        for (Particle current : particles) {
            var force = new Vector(0, 0, 0);
            for (Particle other : subset) {
                if (current != other) {
                    force = force.add(current.getGravitationalForceWithoutSingularities(other));
                }
            }
            force.scale(GRAVITY);

            forces.add(force);
        }

        // step 2: calculate new velocities and positions
        for (int i = 0; i < particles.size(); i++) {
            var f = forces.get(i);
            var p = particles.get(i);
            p.velocity = p.velocity.add(f.scale(dt).scale((double)1/p.mass));
            p.position = p.position.add(p.velocity.scale(dt));
        }
    }
    //needs to be refactored into ParticleSystem class
    private List<Particle> randomSubset(List<Particle> particles, int k) {
        Random random = new Random();
        Collections.shuffle(particles,random);
        return particles.subList(0, k);
    }

}
