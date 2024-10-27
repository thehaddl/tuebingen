import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationTest {
    @Test
    void center_of_gravity_shouldnt_shift() throws IOException {
        Simulation s = new Simulation(100,100);
        ParticleSystem initial = ParticleSystem.createRandomPositions(100,100);
        Vector c1 = initial.centerOfGravity();
        Vector c2 = s.runSim(initial).centerOfGravity();
        Vector diff = c1.subtract(c2);
        var actual = diff;
        assertArrayEquals(new double[]{0, 0, 0}, actual.components,0.0001);

    }
}
