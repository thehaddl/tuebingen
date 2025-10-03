import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationWriterTest {

    @TempDir
    Path dir;

    @Test

    void writeStep_should_write_particles_for_step() throws IOException {

        // setup
        Path pt = dir.resolve("output.csv");
        List<Particle> l = new ArrayList<>();
        l.add(new Particle(new Vector(1, 2, 3), new Vector(2, 3, 4)));
        l.add(new Particle(new Vector(3, 4, 5), new Vector(4, 5, 6)));
        //execute
        l.get(0).putCharge(-1.2);
        try (var c = new SimulationToCsvWriter(pt)) {
            c.writeStep(17, l);
        }


        // Verify
        String actual = Files.readString(pt);

        assertEquals("step,position_x,position_y,position_z,velocity_x,velocity_y,velocity_z,ID,inuse,charge\n17,1.0,2.0,3.0,2.0,3.0,4.0,0,false,-1.2\n17,3.0,4.0,5.0,4.0,5.0,6.0,0,false,0.0\n", actual);


    }
}
