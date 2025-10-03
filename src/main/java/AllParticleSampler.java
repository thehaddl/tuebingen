import java.util.ArrayList;
import java.util.List;

public class AllParticleSampler implements ParticleSampler {
    public List<Particle> select(List<Particle> particles, SimulationProfiler profiler) {
        return new ArrayList(particles);
    }
}
