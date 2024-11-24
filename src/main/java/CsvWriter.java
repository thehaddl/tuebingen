import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CsvWriter implements SimStepOutput, AutoCloseable {

    private final BufferedWriter writer;

    public CsvWriter(Path  file) throws IOException {
        writer = Files.newBufferedWriter(file);
    }

    @Override
    public void writeStep(int round, Particle[] particles) {
        try {
            writer.write("step,postion,velocity,charge,mass\n");
            for (var p : particles) {
                writer.write(String.join(",", String.valueOf(round), toCSV(p.position), toCSV(p.velocity),Double.toString(p.charge),Double.toString(p.charge))+"\n");
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
