import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
enum ForceCalculationMethod {
    NAIVE_IMMUTABLE,NAIVE_MUTABLE, PARALLEL_IMMUTABLE, PARALLEL_MUTABLE
}

class Simulation {
    private final int steps;
    private final double dt;
    private boolean paralell = false;
    private SimStepOutput output = SimStepOutput.NOP;
    private SimulationProfiler profiler = new SimulationProfiler();

    public Simulation(int steps, double runTime) {
        this.steps = steps;
        dt = runTime/steps;

    }
    public void setParralell(boolean p){
        this.paralell = p;
    }
    public void resetProfiler(){
        this.profiler.reset();
    }
    public void setOutput(SimStepOutput output) {
        this.output = output;
    }
    public void setForceCalculationMethod(ForceCalculationMethod method) {
        this.method = method;
    }
    public void setParticleSampler(ParticleSampler ps){
        this.sampler = ps;
    }
    //runs simulation with all particles (n*(n-1) terms)
    public ParticleSystem runSim(ParticleSystem initial){
        profiler.startTimer("runSim");

        List<Particle> particles = initial.getParticles();

        for (int s = 0; s < steps; s++) {
            profiler.startTimer("writeOutput");
            output.writeStep(s, particles);
            profiler.endTimer("writeOutput");
            profiler.startTimer("simStepAll");
            runSimStep(particles, particles);
            profiler.endTimer("simStepAll");
        }
        profiler.endTimer("runSim");

        profiler.printReport();
        this.resetProfiler();
        return ParticleSystem.createFrom(particles);
    }

    //runs simulation with a given subset (only k * n terms)
    public ParticleSystem runSimWithSubset(ParticleSystem initial, int subsetSize){
        profiler.startTimer("runSimWithSubset");
        List<Particle> particles = initial.getParticles();
        for (int s = 0; s < steps; s++) {
            profiler.startTimer("writeOutput");
            output.writeStep(s, particles);
            profiler.endTimer("writeOutput");
            profiler.startTimer("simStepWithSubset");
            runSimStep(particles, randomSubset(particles, subsetSize));
            profiler.endTimer("simStepWithSubset");
            for(Particle p:particles){
                p.inuse=false;
            }
        }
        profiler.endTimer("runSimWithSubset");
        profiler.printReport();
        this.resetProfiler();
        return ParticleSystem.createFrom(particles);

    }
    // calculates a general Simulation Step (if k = n then subset is just all particles, however results differ because of numeric error)

    private void runSimStep(List<Particle> particles, List<Particle> subset) {

        double COUPLINGCONST = 1.0/subset.size();
        List<Vector> forces;
        if(paralell) {
            profiler.startTimer("forceCalculationInParallel");
             forces = particles.parallelStream()
                    .map(current -> {
                        var force = new Vector(0, 0, 0);
                        for (Particle other : subset) {
                            if (current != other) {
                                force = force.add(current.getCouloumbForce(other));
                            } else {
                                current.inuse = true;
                            }
                        }
                        force.scale(COUPLINGCONST);
                        return force;  // Note: removed the scale call above, doing it here
                    })
                    .collect(Collectors.toList());
            profiler.endTimer("forceCalculationInParallel");
        }
        else{
            profiler.startTimer("forceCalculationRegular");
            forces = new ArrayList<>();
            for(Particle current:particles) {

                var force = new Vector(0, 0, 0);
                for (Particle other : subset) {
                    if (current != other) {
                        force = force.add(current.getCouloumbForce(other));
                    } else {
                        current.inuse = true;
                    }
                }
                forces.add(force.scale(COUPLINGCONST));

            }
            profiler.endTimer("forceCalculationRegular");
        }

        // step 2: calculate new velocities and positions
        profiler.startTimer("calculatePositionsAndVelocities");
        for (int i = 0; i < particles.size(); i++) {
            var f = forces.get(i);
            var p = particles.get(i);
            p.velocity = p.velocity.add(f.scale(dt).scale((double)1/p.mass));
            p.position = p.position.add(p.velocity.scale(dt));
        }
        profiler.endTimer("calculatePositionsAndVelocities");
    }
    //needs to be refactored into ParticleSystem class

    private List<Particle> randomSubset(List<Particle> particles, int k) {
        profiler.startTimer("sampleRandomSubset");
        List<Particle> copy = new ArrayList<>(particles);
        Collections.shuffle(copy);
        profiler.endTimer("sampleRandomSubset");
        return copy.subList(0, k);

    }

}
