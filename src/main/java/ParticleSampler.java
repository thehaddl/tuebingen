import java.util.List;

public interface ParticleSampler {
    List<Particle> select(List<Particle> particles,SimulationProfiler profiler);
}

