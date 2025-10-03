import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ForceToCsvWriter extends CsvWriter  {
    private final int upperBoundParticles;

    public ForceToCsvWriter(Path file, int upperBoundParticles) throws IOException {
        super(file);
        this.upperBoundParticles = upperBoundParticles;
        writer.write("particleID,forceX,forceY,forceZ\n");
    }
    public void writeForces(Map<Integer, List<Vector>> forcesByParticle) throws IOException {
        int i = 0;
        for (Map.Entry<Integer, List<Vector>> entry : forcesByParticle.entrySet()) {
            if (i >= upperBoundParticles) break;
            int particleID = entry.getKey();
            List<Vector> forces = entry.getValue();
            for (Vector force : forces) {
                writer.write(particleID + "," + toCSV(force) + "\n");
            }
            i++;
        }
    }
}
