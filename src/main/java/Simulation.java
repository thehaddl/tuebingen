import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
enum ForceCalculationMethod {
    NAIVE_IMMUTABLE,NAIVE_MUTABLE, PARALLEL_IMMUTABLE, PARALLEL_MUTABLE
}

class Simulation {
    private final int steps;
    private final double dt;
    private SimStepOutput output = SimStepOutput.NOP;
    private SimulationProfiler profiler = new SimulationProfiler();
    private ForceCalculationMethod method = ForceCalculationMethod.PARALLEL_MUTABLE;
    private ParticleSampler sampler = new AllParticleSampler();

    public Simulation(int steps, double runTime) {
        this.steps = steps;
        dt = runTime/steps;

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
            List<Particle> sample = sampler.select(particles,profiler);
            profiler.startTimer("simStep");
            runSimStep(particles, sample);
            profiler.endTimer("simStep");
        }
        profiler.endTimer("runSim");

        this.resetProfiler();
        return ParticleSystem.createFrom(particles);
    }

    List<Vector> calculateForcesNaiveImmutable(List<Particle> particles, List<Particle> subset){
        List<Vector> forces;
        double COUPLINGCONST = 1.0/subset.size();
        profiler.startTimer("forceCalculationNaiveImmutable");
        forces = new ArrayList<>();
        for(Particle current:particles) {

            var force = new Vector(0, 0, 0);
            for (Particle other : subset) {
                if (current != other) {
                    force = force.add(current.getGravitationalForce(other));
                } else {
                    current.inuse = true;
                }
            }
            forces.add(force.scale(COUPLINGCONST));

        }
        profiler.endTimer("forceCalculationNaiveImmutable");
        return forces;
    }
    List<Vector> calculateForcesNaiveMutable(List<Particle> particles, List<Particle> subset){
        List<Vector> forces;
        double COUPLINGCONST = 1.0/subset.size();
        profiler.startTimer("forceCalculationNaiveMutable");
        forces = new ArrayList<>();
        for(Particle current:particles) {

            var force = new Vector(0, 0, 0);
            for (Particle other : subset) {
                if (current != other) {
                    force = force.addInPlace(current.getGravitationalForce(other));
                } else {
                    current.inuse = true;
                }
            }
            forces.add(force.scale(COUPLINGCONST));

        }
        profiler.endTimer("forceCalculationNaiveMutable");
        return forces;
    }
    List<Vector> calculateForcesParallelImmutable(List<Particle> particles, List<Particle> subset){
        List<Vector> forces;
        double COUPLINGCONST = 1.0/subset.size();
        profiler.startTimer("forceCalculationParallelImmutable");
        forces = particles.parallelStream()
                .map(current -> {
                    var force = new Vector(0, 0, 0);
                    for (Particle other : subset) {
                        if (current != other) {
                            force = force.add(current.getGravitationalForce(other));
                        } else {
                            current.inuse = true;
                        }
                    }
                    force.scale(COUPLINGCONST);
                    return force;  // Note: removed the scale call above, doing it here
                })
                .collect(Collectors.toList());
        profiler.endTimer("forceCalculationParallelImmutable");
        return forces;
    }
    List<Vector> calculateForcesParallelMutable(List<Particle> particles, List<Particle> subset){
        List<Vector> forces;
        double COUPLINGCONST = 1.0/subset.size();
        profiler.startTimer("forceCalculationInParallelMutable");
        forces = particles.parallelStream()
                .map(current -> {
                    var force = new Vector(0, 0, 0);
                    for (Particle other : subset) {
                        if (current != other) {
                            force = force.addInPlace(current.getGravitationalForce(other));
                        } else {
                            current.inuse = true;
                        }
                    }
                    force.scale(COUPLINGCONST);
                    return force;  // Note: removed the scale call above, doing it here
                })
                .collect(Collectors.toList());
        profiler.endTimer("forceCalculationInParallelMutable");
        return forces;
    }
    List<Vector> calculateForces(List<Particle> particles, List<Particle> sample) {
        return switch (method) {
            case NAIVE_IMMUTABLE -> calculateForcesNaiveImmutable(particles,sample);
            case NAIVE_MUTABLE -> calculateForcesNaiveMutable(particles,sample);
            case PARALLEL_IMMUTABLE -> calculateForcesParallelImmutable(particles, sample);
            case PARALLEL_MUTABLE -> calculateForcesParallelMutable(particles, sample);
        };
    }

    private void runSimStep(List<Particle> particles, List<Particle> sample) {
        List<Vector> forces = calculateForces(particles,sample);
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

}
