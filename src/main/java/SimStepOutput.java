public interface SimStepOutput {

    public static SimStepOutput NOP = (r, p) -> {};

    void writeStep(int round, Particle[] particles);

}
