import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvWriter implements SimStepOutput, AutoCloseable {

    private final BufferedWriter writer;

    public CsvWriter(Path  file) throws IOException {
        writer = Files.newBufferedWriter(file);
        writer.write("step,position_x,position_y,position_z,velocity_x,velocity_y,velocity_z,charge,mass\n");
    }

    @Override
    public void writeStep(int round, List<Particle> particles) {
        try {
            for (var p : particles) {
                writer.write(String.join(",", String.valueOf(round), toCSV(p.position), toCSV(p.velocity),Double.toString(p.charge),Double.toString(p.mass))+"\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toCSV(Vector v) {
        return Arrays.stream(v.components).mapToObj(Double::toString).collect(Collectors.joining(","));
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
