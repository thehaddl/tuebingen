import java.util.List;

public interface SimStepOutput {

    public static SimStepOutput NOP = (r, p) -> {};

    void writeStep(int round, List<Particle> particles);

}
