import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomParticleSampler  implements ParticleSampler{
    private int sampleSize;

    public RandomParticleSampler(int sampleSize) {
        this.sampleSize = sampleSize;
    }
    public void setSampleSize(int sampleSize){
        this.sampleSize = sampleSize;
    }

    public List<Particle> select(List<Particle> particles, SimulationProfiler profiler) {
        profiler.startTimer("sampleRandomSubset");
        List<Particle> copy = new ArrayList<>(particles);
        Collections.shuffle(copy);
        List<Particle> sample = copy.subList(0, Math.min(sampleSize, particles.size()));
        profiler.endTimer("sampleRandomSubset");
        return sample;
    }
}
