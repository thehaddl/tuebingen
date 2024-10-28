import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationTest {
    @Test
    void center_of_gravity_shouldnt_shift() throws IOException {
        Simulation s = new Simulation(100,1000,100,100);
        Vector c1 = s.centerOfGravity(s.initialParticles);

        Vector c2 = s.centerOfGravity(s.runSim());
        Vector diff = c1.subtract(c2);
        System.out.println(diff.toString());

    }
}
