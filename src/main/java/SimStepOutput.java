import java.util.List;

public interface SimStepOutput {

    SimStepOutput NOP = (r, p) -> {};

    void writeStep(int round, List<Particle> particles);

}
