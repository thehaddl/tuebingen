import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvWriterTest {

    @TempDir
    Path dir;

    @Test

    void writeStep_should_write_particles_for_step() throws IOException {

        // setup
        Path pt = dir.resolve("output.csv");

        //execute
        try (var c = new CsvWriter(pt)) {
            c.writeStep(17, new Particle[]{
                    new Particle(new Vector(1, 2, 3), new Vector(2, 3, 4)),
                    new Particle(new Vector(3, 4, 5), new Vector(4, 5, 6))
            });
        }

        // Verify
        String actual = Files.readString(pt);
        assertEquals("step,position_x,position_y,position_z,velocity_x,velocity_y,velocity_z,ID,inuse\n17,1.0,2.0,3.0,2.0,3.0,4.0,0,false\n17,3.0,4.0,5.0,4.0,5.0,6.0,0,false\n", actual);

    }
}
