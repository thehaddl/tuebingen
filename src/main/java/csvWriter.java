import java.io.FileWriter;
import java.io.IOException;

public class csvWriter {
    private final String filePath;
    csvWriter(String pathToFile){
        this.filePath = pathToFile;
    }
    void writeParticlesToCsv(Particle[] particles) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Position_X,Position_Y,Position_Z,Velocity_X,Velocity_Y,Velocity_Z\n");
            for (Particle p : particles) {
                writer.write(p.toCsvString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
