import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SimulationToCsvWriter extends CsvWriter implements SimStepOutput{
    public SimulationToCsvWriter(Path file) throws IOException {
        super(file);
        writer.write("step,position_x,position_y,position_z,velocity_x,velocity_y,velocity_z,ID,inuse,charge\n");
    }
    @Override
    public void writeStep(int round, List<Particle> particles) {
        try {
            for (var p : particles) {

                writer.write(String.join(",", String.valueOf(round), toCSV(p.position), toCSV(p.velocity),Integer.toString(p.id),Boolean.toString(p.inuse), p.charge +"\n"));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
